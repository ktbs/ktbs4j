#!/usr/bin/ruby
require 'webrick'
include WEBrick

class AccueilServlet < HTTPServlet::AbstractServlet
def do_GET(req, res)
  if req.path=='/dino/0/alarme'
    redirectPath = '/dino/0/'
    puts "redirecting to #{redirectPath}"
    res.status = 303
    res['Location'] = "http://#{req.host}:#{req.port}#{redirectPath}"
  elsif req.path=='/dino/0/'
    res.body = IO.read "/home/dcram/dev/ws-visu2/ktbs-2-tests/src/org/liris/ktbs/debug/rdfparser/model-0-alarme.rdf"
    res['Content-Type'] = "application/rdf+xml"
  else
    res.status = 404
  end
end
end

# ———————————————–
# Demarrage et configuration du serveur
# ———————————————–
s = HTTPServer.new( :Port => 8001 )
s.mount("/", AccueilServlet)
trap("INT"){ s.shutdown }
s.start
