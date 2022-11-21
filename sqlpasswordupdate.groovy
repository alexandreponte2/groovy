pipeline {
  agent any
  environment {
    CLOUDSDK_CORE_PROJECT='hopeful-summer-368213'
  }
parameters {
    string(name: 'SECRET_ID', defaultValue: 'default', description: 'Secret Name.')
    string(name: 'INSTANCE_ID', defaultValue: 'default', description: 'Project Name passar entre aspas duplas.')
    string(name: 'PROJECT_ID', defaultValue: 'default', description: 'Project Name passar entre aspas duplas.')

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
          gcloud auth activate-service-account --key-file="$GCLOUD_CREDS"
          gcloud config set project $PROJECT_ID
          BUILD_ID=$(gcloud secrets versions access latest --secret=$SECRET_ID --project=$PROJECT_ID)

          echo $BUILD_ID

          gcloud sql users set-password root --host=% --instance=$INSTANCE_ID --password=$BUILD_ID



          '''
        }
      }
    }
  }
}
