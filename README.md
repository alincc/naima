# naima
##Naima: Not Another Intelligent Medical Assistant   

#### Developed with passion @ SequoiaHack::2016 by [Prashant KS](https://in.linkedin.com/in/ksprash) , [Saurabh Minni](https://in.linkedin.com/in/100rabh) and [Tarun Rathor](https://in.linkedin.com/in/tarun-rathor-173aa64)

##Vision:
Provide assisted expert health care to remote patients in an easy and fast way.

##Problem:
In indian rural and semi-urban context access to quality health care is very sparse.
Locally available doctors may not be most experienced or informed to evaluate patients medical
condition with consistency and quality.

##Solution:
We propose to empower the doctor/ care agents at rural health centers with a system, that :-
* Guide them collect most relevant examination data for the medical condition.
* Enable online review of the case with a relevant speciality expert.

System improves over time in following manner:-
* Experts update the system with more knowledge as they review the cases.
* Latest medical data curated periodically to improve examination process.
* Treatment outcomes will be feedback to the system in near realtime to finetune and localize.

## System Architecture

### Overview
System is modelled as a domain driven design with service components implemented as microservices using Node.JS leveraging MongoDB. Analytics consist of batch and streaming components in Apache Spark integrating with services using Kafka. Application components are developed as native android application.

![System Architecture Diagram](https://github.com/tarunjr/naima/blob/master/docs/Naima_System_Architecture.jpg)

### Data Design
Following domain entities will be modelled in the system.
* Patient : A human who has the medical condition
* Doctor: A qualified doctor who can administer treatment.
* Care Provider: A trained human who can collect diagnostic data. E.g Nurse, Trainee
* Symptom: An indicator of a medical condition. E.g Bloating, Burning
* Test: A medical diagnostic test done in lab. E.g Blood Test
* Condition: A medical situation, disease, allergy, reaction or complication. E.g  Acidity
* Speciality: A segment of medical expertise. E.g  Neurology, Gastroenterology etc.
* Treatment: A prescribed drug, therapy or action which will address the condition
* Case: A record which which details the diagnosis and treatment of patient condition.

### Services Design
Following are the runtime services component of the system.

* Users Service: Manages Patient, Doctors and  Care Providers entities.Developed in Node.JS and MongoDB. Exposes REST API

* Case Service: Manages Case entity and workflow between Doctor/Care Provider. Developed in Node.JS and MongoDB. Exposes REST API

* Diagnostic Service: Maintains a repository of medical knowledge about Condition, Symptom, Test and Speciality. Using this knowledge and additional context It generates ranked ordering of Symptom and Test questions to be asked/collected next. Developed in Spring.Boot/Java and Redis. Uses the result of the Analytical component to adapt its behavior.

### Analytics Design:
Following are the analytics components. Apache Spark is the runtime framework using Kafka as data ingestion and output
component.

* Symptoms association rules mining: A batch component in Apache Spark using FP-Growth algorithm from spark.mllib

* Conditions Aggregation by region: A streaming component in Apache Spark using spark.streaming to perform windowed aggregation of conditions diagnosed per region.

* Conditions/Speciality prediction: A nearest neighbour class of algorithm to classify a symptom to a speciality for doctor recommendation.


### Application Design:
Application components are implemented a native android applications interacting with only the services component.
Following two are the application components.

* Care Provider UX: User interface for Examiner to prepare a Case and manage the Review and Treatment. Developed as a native Android application

* Doctor UX: User interface for the Doctor to Review the Case and finalize the Treatment. Developed as a native Android application.
