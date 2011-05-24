from __future__ import with_statement

def check_sources_and_parameters(sources, parameters):
    if sources:
        return "the helloworld transformation accept no sources"
    if parameters:
        return "the helloworld transformation accept no parameter"

def get_model_and_origin(computed_trace):
    return "http://example.com/my-trace-model", "1970-01-01T00:00:00"

def process(computed_trace, sources, parameters, state):
    obsel_type = "http://example.com/my-trace-model#my-obsel-type"
    attr_type = "http://example.com/my-trace-model#my-attr-type"
    if state is None:
        with computed_trace.add_obsel(obsel_type, 1000, 2000) as obs:
            obs.add_out(attr_type, "hello")
        return True, False, 1
    else:
        with computed_trace.add_obsel(obsel_type, 2000, 3000) as obs:
            obs.add_out(attr_type, "world")
        return True, True, []

