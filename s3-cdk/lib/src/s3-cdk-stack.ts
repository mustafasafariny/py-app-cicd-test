//The code is creating a new stack named "S3BucketCdkStack" using the S3CdkStack class.
// This stack represents an AWS CloudFormation stack that will be deployed to create an S3 bucket.

import * as cdk from 'aws-cdk-lib';
//import * as iam from 'aws-cdk-lib/aws-iam';
import * as kms from 'aws-cdk-lib/aws-kms';
import * as s3 from 'aws-cdk-lib/aws-s3';
import { Construct } from 'constructs';

export class S3CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    console.log('hello s3 bucket stack');
    const s3Bucket = new s3.Bucket(this, 'CicdDemoBucket', {
      objectOwnership: s3.ObjectOwnership.BUCKET_OWNER_ENFORCED,
      blockPublicAccess: s3.BlockPublicAccess.BLOCK_ALL,
      encryption: s3.BucketEncryption.S3_MANAGED,
      //encryptionKey: new kms.Key(this, 's3BucketKMSKey'),
      //enforceSSL: true,
      versioned: true,
    });

    //s3Bucket.grantRead(new iam.AccountRootPrincipal());
  }
}

const app = new cdk.App();
new S3CdkStack(app, 'S3BucketCdkStack');
//app.synth();