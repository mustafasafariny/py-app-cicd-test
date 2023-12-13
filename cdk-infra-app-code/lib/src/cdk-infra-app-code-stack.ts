import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as s3 from 'aws-cdk-lib/aws-s3';
import * as iam from 'aws-cdk-lib/aws-iam';
import * as kms from 'aws-cdk-lib/aws-kms';

export class CdkInfraAppCodeStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // S3 Bucket resource
    const s3Bucket = new s3.Bucket(this, 'CicdDemoBucket', {
      objectOwnership: s3.ObjectOwnership.BUCKET_OWNER_ENFORCED,
      blockPublicAccess: s3.BlockPublicAccess.BLOCK_ALL,
      encryption: s3.BucketEncryption.S3_MANAGED,
      versioned: true,
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });
/*
    // Create an IAM role for Jenkins
    const jenkinsRole = new iam.Role(this, 'JenkinsRole', {
        assumedBy: new iam.ServicePrincipal('jenkins.amazonaws.com'),
      });
  
    // Attach the AmazonS3FullAccess policy to the Jenkins role
    jenkinsRole.addManagedPolicy(iam.ManagedPolicy.fromAwsManagedPolicyName('AmazonS3FullAccess'));
  
    // Output the bucket name and role ARN (for Jenkins configuration)
    new cdk.CfnOutput(this, 'ArtifactBucketName', {
        value: s3Bucket.bucketName,
      });
  
    new cdk.CfnOutput(this, 'JenkinsRoleArn', {
        value: jenkinsRole.roleArn,
      });
*/
  }
}
