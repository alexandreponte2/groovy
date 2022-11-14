pipeline {
  agent any
  environment {
    CLOUDSDK_CORE_PROJECT='projectid'
  }
parameters {
    string(name: 'FUNCTION_NAME', defaultValue: 'default', description: 'Function name.')
    string(name: 'ENTRY_POINT', defaultValue: 'default', description: 'Function entry-point.')
    string(name: 'PUBSUB_TOPIC', defaultValue: 'default', description: 'Pub/Sub Topic Name.')
    string(name: 'PROJECT_NAME', defaultValue: 'default', description: 'Project Name passar entre aspas duplas.')

}
  stages {
   stage ('Clean Workspace'){
     steps{
       deleteDir()
      }
    }
    stage('Clone') {
    steps {
        git url: 'https://github.com/alexandreponte2/functionGCP.git', branch: 'main'
      }
    }
    stage('ta aqui') {
    steps {
        echo "==================================================++++===============\n"
        sh 'ls -lha'
      }
    }
    stage('test') {
      steps {
        withCredentials([file(credentialsId: 'gcloud-creds', variable: 'GCLOUD_CREDS')]) {
          sh '''
            gcloud auth activate-service-account --key-file="$GCLOUD_CREDS"
            cd src/
            gcloud functions deploy $FUNCTION_NAME \
               --entry-point=$ENTRY_POINT \
               --source=. \
               --runtime=python310 \
               --allow-unauthenticated \
               --trigger-topic=$PUBSUB_TOPIC \
               --project $PROJECT_NAME
          '''
        }
      }
    }
  }
}
