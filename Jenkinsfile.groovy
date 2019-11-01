node {
    stage('GitPull') {
        // SourceCommit  리파지토리에서 소스 가져오기
        echo "123123333"
		git "https://github.com/pio500/pipline-vue-test.git"
    }
    stage('Test') { 
        echo "Test : ${workspace}"
        sh "ls"
    }
    stage('Build') {
        sh "npm install"
        sh "npm build"
        sh "mkdir node_capsule"
        sh "cd node_capsule"
        git "https://github.com/beewee22/static-server-spa.git"
        cp -r ../dist /dist
        sh "cat << EOF >> Dockerfile \
        FROM node:lts-alpine \
        RUN npm install -g http-server \
        WORKDIR /app \
        COPY package*.json ./ \
        RUN npm ci \
        COPY . . \
        RUN ls -al \
        EXPOSE 80 \
        CMD [ 'npm', 'run', 'serve'] \
        EOF" 
        sh "cat Dockerfile"
    }
    stage('Deploy') {
        archive '**/target/*.war'
    } 
    stage('Deploy') {        
        
        // "빌드 결과물을 objectstorage에 백업한다."
   		sh "python /home/devtrack/pyscript/backup.py"
        
        //"빌드 결과물을 ObjectStorage에 Upload한다"
    	sh "python /home/devtrack/pyscript/upload.py"
    	        
        //"stop springboot ~"
        sh "ssh -v -o StrictHostKeyChecking=no root@(your server ip) sh /home/devtrack/script/runNcp.sh stop"
        
        //"copy artipact  to remote server  over ssh !!"
        sh "scp -p -r  ./target/*.war root@(your server ip):/home/devtrack/deploy"
        
        //"start springboot ~"
        sh "ssh  -o StrictHostKeyChecking=no root@(your server ip) sh /home/devtrack/script/runNcp.sh start"
    }   
}
