set :stage, :production

server "webapps@tomcat.hostnucleus.ca", roles: %w{app}, primary: true
