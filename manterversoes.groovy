pipeline {
  agent any
  environment {
    CLOUDSDK_CORE_PROJECT='projectID'
  }
parameters {
    string(name: 'SECRET_ID', defaultValue: 'default', description: 'Secret Name.')
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
          existentes=$(gcloud secrets versions list $SECRET_ID --project $PROJECT_ID | grep enabled |sort -n | awk '{print$1}' | wc -l)
          manter=$(gcloud secrets versions list $SECRET_ID --project $PROJECT_ID | head -3 | tail -1| awk '{print$1}')

          echo $existentes
          echo $manter

          x=1; while  [ $x -lt $manter ]; do gcloud secrets versions destroy $x --secret=$SECRET_ID --project $PROJECT_ID --quiet; x=$(( $x + 1 )); done || true
          '''
        }
      }
    }
  }
}
