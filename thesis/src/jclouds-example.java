// init
BlobStoreContext context = ContextBuilder
				.newBuilder("aws-s3")
				.credentials(accesskeyid, secretaccesskey)
				.buildView(BlobStoreContext.class);

BlobStore blobStore = context.getBlobStore();

// create container
blobStore.createContainerInLocation(null, "mycontainer");
  
// add blob
Blob blob = blobStore
		.blobBuilder("test")                                                                                   
		.payload("testdata")
		.build();
blobStore.putBlob("mycontainer", blob);
