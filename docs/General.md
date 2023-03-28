# Introduction and Goals

The CRC 901 On-The-Fly Computing has quite ambitious goals, which are worked on by a variety of working groups. One challenge of the CRC is to bring together the concepts and technologies developed by the working groups (which come from different areas of research, including Computer Science and Economics). This is where the Proof of Concept (PoC) comes into play.


In the PoC, the research and developments of the various subprojects flow together in a uniform, integrated software system. With the help of the PoC, typical scenarios of an On-The-Fly market can be played through. The goals of the PoC are:

- Demonstration of the general feasibility of On-The-Fly Computing
- Imparting the concepts of On-The-Fly Computing to both a specialist audience and lay people
- Clarification of the relationships between the subprojects
- Testing new approaches and reviewing hypotheses



# OTF Machine Learning Scenario

The PoC is designed domain agnostic, such that it can be adopted for any kind of OTF problems. For illustration purposes, the PoC focuses on an application scenario to configure tailor-made Machine Learning Services on the fly. The main problem addressed is the automatic creation of a learning process that generates a predictive model (e.g., a classifier) ​​on the basis of given training data. Such a learning process consists of different algorithms or software components that have to be appropriately parameterized and combined with each other. The result of a corresponding selection and configuration process is a Machine Learning Pipeline. This OTF Machine Learning Scenario has the characteristics of an OTF problem:

- end-users do not need to know the underlying software architecture and manual steps in this process,
- the user is provided with an executable service in the form of a Machine Learning pipeline,
- this service is optimized for the individual requirements (i.e. training data) and
- the provision of the service takes place within a short time.


# Contributions:

<img src="pics/SFB_901_Logo_Final_blau.svg" alt="Logo of the Collaborative Research Center 901" width="200"/>

Numerous concepts and methods of OTF computing, which have been developed in the respective subprojects, have already been integrated in the proof of concept.

- A1: Self-stabilizing publish-subscribe system for market and OTF provider
- A3, A4: Concepts for the reputation system for the rating of service compositions
- B1: Chatbot for a user-friendly requirements specifications and a matcher for matching non-functional requirements
- B2: Configurator for service composition using heuristic search
- B3: Verification of functional properties within operation sequences
- B4: Certification and validation of functional properties of basic services
- C1: Authentication of ratings, authorization for buying service compositions and their access control
- C2, C4: Deployment of basic services in Compute Centers and the execution of service compositions in heterogeneous computing environments
- C5: Conformance checking of the Proof-of-Concept architecture with the On-the-Fly architecture framework
