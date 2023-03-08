# About

![PoC logo](docs/pics/PoCLogo.png)

The *Collaborative Research Center 901* (CRC901) "On-the-fly computing" is a [research project](https://sfb901.uni-paderborn.de) driven by Paderborn University with the goal of developing concepts and techniques for composing user-specific software solutions on the fly. The basic idea is that an end user provides her requirements in a non-technical form and receives an individual piece of software fulfilling these requirements.

This repository contains the *Proof of Concept* (PoC), the demonstrator of  the CRC901. It brings together the research results of many of the involved working groups in a piece of software realizing the on-the-fly approach. The domain chosen for demonstration purposes is *machine learning*: given the user's requirements, the PoC will create a machine learning pipeline realizing these requirements, and makes that pipeline available as a web service.

This documentation is targeted at developers who want to make use of some or all of the PoC's services. It is structured as follows:

* A [general introduction](docs/General.md) to the PoC and its goals is given.
* The PoC's [architecture](docs/Architecture.md) is described on the component and deployment level, including sequence diagrams showing the system's flow of information.
* Developing and building the PoC requires a certain development environment; [setting up that development environment](docs/set-up-dev-environment.md) is described next.
* The PoC needs a repository manager such as [Nexus](https://www.sonatype.com/products/nexus-repository) to store the generated ML solutions. How to setup a Nexus instance is described [here](docs/setup-nexus-repository-with-nginx.md).
* We then describe how to actually [build the PoC services](docs/build-poc-services.md).
* The PoC services need to be deployed to a [Kubernetes](https://kubernetes.io/) cluster. The setup of a cluster appropriate for running the PoC is described [here](https://github.com/CRC-901-On-the-Fly-Computing/Ansible-k8cluster).
* Finally, the [deployment of the PoC to the Kubernetes cluster](docs/deploy-services-to-k8-cluster.md) is described.


The PoC code is organized into different Git repositores:

* This repository: The main code base of the POC, including its web frontend
* [Ansible-k8cluster](https://github.com/CRC-901-On-the-Fly-Computing/Ansible-k8cluster): A set of [Ansible](https://www.ansible.com/) scripts helping to set up a Kubernets cluster as need by the PoC
* [Helm-Charts](https://github.com/CRC-901-On-the-Fly-Computing/Helm-Charts): a set of [Helm](https://helm.sh/) scripts helping to deploy the PoC to a Kubernetes cluster
* [ServiceCodeProvider](https://github.com/CRC-901-On-the-Fly-Computing/ServiceCodeProvider): The basic services which the PoC uses to compose its ML solutions
* [executor-bootup](https://github.com/CRC-901-On-the-Fly-Computing/executor-bootup): Support for implementing PoC services in different technologies

This repository and its dependencies have been provided in the hope of being useful. If you want to try the PoC, or if you want to reuse one or more of its components, feel free to do so! And if you happen to need support or feel like providing us feedback, please contact TODO (note that the CRC has reached its end in July 2023, and most of the involved researchers have moved on, so it might be difficult to provide you with an appropriate contact, but we will do our best).
