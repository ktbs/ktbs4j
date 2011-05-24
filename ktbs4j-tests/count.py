'''
I count the obsels in all the sources, and generate one obsel by time window (see below) containing the number of counted obsel as an attribute.

The width of the time window is given as a parameter 'window', and defaults to 1.
'''
from __future__ import with_statement

def check_sources_and_parameters(sources, parameters):
    window = parameters.pop("window", 1)
    try:
        int(window)
    except ValueError:
        return "parameter 'window' should be an integer"
    if parameters:
        return "unrecognized parameters %s" % " ".join(parameters)

def get_model_and_origin(transformed_trace):
    # TODO we should give an existing trace model
    # TODO we should check that the sources have compatible time domains and
    #      origins
    return "http://example.com/counting-model", "1970-01-01T00:00:00"

def process_obsel(obsel, transformed_trace, sources, parameters, state):
    if state is None:
        window = int(parameters.get('window', 1000))
        current = window-1
        state = { 'window':window, 'set':set(), 'current':current, }
    else:
        window = state['window']
        current = state['current']

    ## just a test to explore access to obsel properties
    #print "---", obsel
    #for i in obsel.outgoing:
    #   print "---", " ->", i
    #    for j in obsel.outgoing[i]:
    #       print "---", "   ", j
    #for i in obsel.incoming:
    #    print "---", " <-", i
    #    for j in obsel.incoming[i]:
    #        print "---", "   ", j

    #print "===", state
    modified = False
    while obsel.end > current:
        current += window
        modified = True
    if modified:
        obsels = state['set']
        if obsels:
            obsel_type = "http://example.com/counting-model#Count"
            attr_type = "http://example.com/counting-model#value"
            end = state['current']
            begin = end + 1 - state['window']
            with transformed_trace.add_obsel(
                 obsel_type, begin, end, source_obsels=obsels
            ) as o:
                o.add_out(attr_type, str(len(obsels)))
        state['current'] = current
        state['set'] = set([str(obsel.node)])
    else:
        state['set'].add(str(obsel.node))
    return modified, False, state

