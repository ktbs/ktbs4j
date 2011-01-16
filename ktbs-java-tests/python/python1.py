from __future__ import with_statement

def check_sources_and_parameters(sources, parameters):
    if parameters:
        return "the helloworld transformation accept no parameter"

def get_model_and_origin(transformed_trace):
    return "http://example.com/my-trace-model", "1970-01-01T00:00:00"

def process_obsel(obsel, transformed_trace, sources, parameters, state):
    obsel_type = "http://example.com/my-trace-model#my-obsel-type"
    attr_type = "http://example.com/my-trace-model#my-attr-type"
    with transformed_trace.add_obsel(obsel_type, 42, 101, "foo") as obs:
        obs.add_out(attr_type, "hello world")
    return True, True, []
