# Introduction

The *Collaborative Research Center 901* (CRC901) "On-the-fly computing" is a [research project](https://sfb901.uni-paderborn.de) driven by Paderborn University with the goal of developing concepts and techniques for composing user-specific software solutions on the fly. The basic idea is that an end user provides her requirements in a non-technical form and receives an individual piece of software fulfilling these requirements.

This repository contains the *Proof of Concept* (PoC), the demonstrator of  the CRC901. It brings together the research results of many of the involved working groups in a piece of software realizing the on-the-fly approach. The domain chosen for demonstration purposes is *machine learning*: given the user's requirements, the PoC will create a machine learning pipeline realizing these requirements, and makes that pipeline available as a web service.

This documentation is targeted at developers who want to make use of some or all of the PoC's components. It is structured as follows:
* A [general introduction](General.md) to the CRC901 in general and the PoC in particular is given
* The PoC's [architecture](Architecture.md) is described on the component and deployment level
* The [development](Development.md) part describes how to build, run, and debug the PoC, and provides some insights into the PoC's components
* Finally, the [DevOps](DevOps.md) part deals with setting up the PoC's development and runtime infrastructure
