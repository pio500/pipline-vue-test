node {
    stage('Preparation') { 
        // for display purposes
        echo "Current workspace : ${workspace}"
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
    }
    stage('Checkout') {
        // SourceCommit  리파지토리에서 소스 가져오기
        echo "123123333"
		git "https://github.com/pio500/pipline-vue-test.git"
		sh "npm install"
		sh "ls"
    }
    stage('Test') {
		echo "Test"
        sh "ls"
		echo "EndTest"
        println "skip Test"        
        //sh "'${mvnHome}/bin/mvn'  -Dmaven.test.failure.ignore -B verify"
            
    }
    stage('Build') {
    
    	// maven 빌드    	
        sh "'${mvnHome}/bin/mvn'  -Dmaven.test.skip=true  clean install package"
    }
    stage('Archive') {
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
