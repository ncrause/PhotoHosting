set :application, 'PhotoHosting'
set :repo_url, "https://github.com/ncrause/PhotoHosting.git"
set :deploy_to, "/home/webapps/#{fetch(:application)}"
set :branch, ENV['BRANCH'] || ENV['branch'] || "master"
set :ssh_options, { :keys => ["#{ENV['HOME']}/.ssh/id_rsa"] }
set :keep_releases, 5

namespace :deploy do

	task :assemble do
		on roles :app do
			within release_path do
				execute *%w{mvn clean compile war:exploded}
			end
		end
	end
	
	task :restart do
		on roles :app do
			execute *%w{sudo service tomcat9 restart}
		end
	end

  before :publishing, :assemble
	after :published, :restart
			
end
