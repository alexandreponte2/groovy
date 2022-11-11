pipeline {
  agent any
  environment {
    CLOUDSDK_CORE_PROJECT='PROJECT_ID'
  }
  stages {
    stage ('Clean Workspace'){
        steps{
            deleteDir()
       }
    }
    stage('versoes') {
      steps {
        withCredentials([file(credentialsId: 'gcloud-creds', variable: 'GCLOUD_CREDS')]) {
          sh '''
          x=1; while  [ $x -lt 5 ]; do echo "Welcome $x times"; x=$(( $x + 1 )); done
          '''
        }
      }
    }
  }
}
