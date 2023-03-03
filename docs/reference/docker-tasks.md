# Docker Tasks

These are the docker-related Gradle tasks used in the project.

- docker : Builds Docker image
- dockerClean : Cleans Docker build directory
- dockerfileZip : Bundles the configured Dockerfile in a Zip file
- dockerPrepare : Prepares Docker build directory
- dockerPush : Pushes named Docker image to configured Docker Hub
- dockerPushDate : Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:2020-05-01T08_37_54' to configured Docker Hub
- dockerPushLatest : Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:latest' to configured Docker Hub
- dockerPushVersion : Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:unversioned' to configured Docker Hub
- dockerTag : Applies all tags to the Docker image
- dockerTagDate : Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:2020-05-01T08_37_54'
- dockerTagLatest : Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:latest'
- dockerTagsPush : Pushes all tagged Docker images to configured Docker Hub
- dockerTagVersion : Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:unversioned'