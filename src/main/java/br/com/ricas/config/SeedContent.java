package br.com.ricas.config;

import com.mongodb.client.MongoClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeedContent implements ApplicationRunner {

	@Value("${spring.mongodb.database}")
	private String database;

	@Value("${spring.ai.vectorstore.mongodb.collection-name}")
	private String collection;

	private final VectorStore vectorStore;

	private final MongoClient mongoClient;

	public SeedContent(VectorStore vectorStore, MongoClient mongoClient) {
		this.vectorStore = vectorStore;
		this.mongoClient = mongoClient;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (mongoClient.getDatabase(database).getCollection(collection).countDocuments() > 0) {
			return;
		}

		var listDocuments = List.of(
				new Document("Profile / perfil / biography /" +
						" biografia of Ricardo Mello. " +
						" Ricardo Mello is a backend engineer, international speaker, open-source contributor" +
						"and Senior Developer Advocate at MongoDB, based in São Paulo, " +
						"Brazil. He has 16 years of experience in software development, " +
						"with approximately 14 years primarily focused on backend engineering" +
						" and delivering production systems for large companies across industries" +
						" such as banking, logistics, and retail. Throughout most of his career, " +
						" Ricardo worked directly with software analysis, architecture, and development" +
						" — from understanding business requirements and real customer problems to " +
						"designing, implementing, optimizing, and delivering reliable solutions to " +
						"production. His main technical background is in Java, Kotlin, Spring Boot," +
						" Spring Data MongoDB, MongoDB, microservices, PostgreSQL, REST APIs, " +
						"Docker, and event-driven architectures with technologies such as Kafka. " +
						"He has also worked beyond backend development, including Android application" +
						" development and web applications using technologies such as Angular. " +
						"His career includes building large-scale warehouse management and " +
						"logistics systems, including WMS platforms used by Amazon facilities, " +
						"backend systems in the retail industry, and financial applications at C6 Bank," +
						"where he worked with Kotlin microservices for TechInvest, investment products," +
						" and Pix. Sharing knowledge has always been an important part of" +
						" Ricardo's career. Even while working primarily as a software engineer," +
						" he enjoyed helping other developers, explaining technical concepts, " +
						"sharing practical experience, and helping teams solve engineering problems." +
						" Today, as a Senior Developer Advocate at MongoDB, he combines this hands-on" +
						" engineering background with developer education, helping Java and Kotlin " +
						"developers build better applications using technologies such as MongoDB," +
						" the MongoDB Java Driver, Spring Boot, Spring Data MongoDB, Spring AI," +
						"Vector Search, Retrieval-Augmented Generation (RAG), and modern " +
						"AI-powered application patterns. He does this through technical content, talks," +
						" workshops, hands-on labs, real-world projects, and direct engagement with " +
						"developer communities. He also continues to expand his open-source" +
						" contributions within the Java and Spring ecosystems, particularly around " +
						"projects and integrations involving Spring Data, Spring AI, and MongoDB." +
						" Personal website: https://ricardohsmello.com.",
						Map.of(
								"title", "Ricardo Mello - Professional Profile",
								"category", "profile",
								"createdAt", "2026-08-10"
						)
				),
					new Document(
							"Ricardo Mello's professional experience at MongoDB. "
									+ "Role: Senior Developer Advocate. "
									+ "Period: Jun 2024 — Present. "
									+ "Location: São Paulo, Brazil · Remote. "
									+ "Scope: Global. "
									+ "Responsibilities and achievements: Ricardo focuses on the Java and Kotlin developer ecosystems and leads Java-focused initiatives within Developer Relations at MongoDB. "
									+ "A core part of his role is understanding how Java, Kotlin, and their surrounding ecosystems are evolving in the market and determining how developers can use these technologies effectively with MongoDB. "
									+ "This responsibility works in both directions: when new versions, frameworks, or capabilities emerge in the Java ecosystem, he evaluates how they integrate with MongoDB; when MongoDB introduces new products or features, he explores how Java and Kotlin developers can adopt and apply them in real-world applications. "

									+ "This work keeps him closely connected with MongoDB engineering and product teams, including the Java Driver team, where he participates in technical discussions about new features, developer experience, integrations, and upcoming capabilities. "
									+ "He also works closely with the broader Java and Spring ecosystems, including collaboration and technical discussions around Spring Data MongoDB, to understand new developments, evaluate MongoDB integrations, provide developer feedback, and contribute to open-source initiatives. "

									+ "His technical work spans areas such as the MongoDB Java Driver, Spring Boot, Spring Data MongoDB, Spring AI, Quarkus, data modeling, the Aggregation Framework, Queryable Encryption, Atlas Search, Vector Search, Retrieval-Augmented Generation (RAG), AI-powered applications, microservices, event-driven architectures, Kafka, messaging systems, and distributed application architecture. "

									+ "Ricardo also works directly with MongoDB customers and engineering teams, including large enterprise organizations, participating in technical sessions and architecture Design Reviews. "
									+ "In these engagements, he helps teams analyze existing architectures, understand application and data requirements, identify potential improvements, and apply MongoDB effectively in production systems. "
									+ "These discussions range from foundational topics such as document modeling, schema design, query patterns, and application integration to more advanced architectural topics including microservices, event-driven systems, Kafka and messaging platforms, scalability, distributed systems, and horizontal scaling. "

									+ "Another major part of his role is developer enablement. He is responsible for developing and leading global technical enablement programs, hands-on workshops, labs, demos, and reusable educational content that help developers build applications using Java, Kotlin, Spring, Quarkus, and MongoDB. "
									+ "These initiatives support developers and technical teams across different regions and range from MongoDB fundamentals to advanced application architecture and AI-related use cases. "

									+ "He also supports the broader developer community through international conference talks, enterprise technical sessions, mentoring, technical content, real-world projects, and open-source contributions within the Java, Spring, and MongoDB ecosystems. "

									+ "Skills and technologies: Java, Kotlin, Spring Boot, Spring Data MongoDB, Spring AI, Quarkus, MongoDB, MongoDB Java Driver, Data Modeling, Aggregation Framework, Queryable Encryption, Atlas Search, Vector Search, RAG, AI applications, Microservices, Kafka, Event-Driven Architecture, Distributed Systems, Developer Relations, Developer Enablement, Architecture Design Reviews, Workshops, and Developer Education. "

									+ "Company website: https://www.mongodb.com.",
							Map.of(
									"title", "Senior Developer Advocate at MongoDB",
									"category", "experience",
									"createdAt", "2026-08-10"
							)
					),
				new Document(
						"Ricardo Mello's professional experience at C6 Bank. Role: Senior Software Engineer. Period: Mar 2021 — Jun 2024. Location: São Paulo, Brazil · Remote. Responsibilities and achievements: Helped build TechInvest from the ground up and developed Kotlin microservices for investment products and Pix, used by millions of customers. Skills and technologies: Kotlin, MongoDB, PostgreSQL, Kafka, Microservices. Company website: https://www.c6bank.com.br.",
						Map.of(
								"title", "Senior Software Engineer at C6 Bank",
								"category", "experience",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Ricardo Mello's professional experience at Luizalabs. Role: Senior Software Engineer. Period: Sep 2020 — Mar 2021. Location: Ribeirão Preto, Brazil. Responsibilities and achievements: Created and maintained Java and Node.js backend services with PostgreSQL and Amazon SQS. Skills and technologies: Java, Node.js, PostgreSQL, Amazon SQS. Company website: https://www.magazineluiza.com.br.",
						Map.of(
								"title", "Senior Software Engineer at Luizalabs",
								"category", "experience",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Ricardo Mello's professional experience at Senior Sistemas. Role: Senior Software Engineer. Period: Oct 2012 — Sep 2020. Location: Ribeirão Preto, Brazil. Responsibilities and achievements: Built Java solutions and large-scale warehouse management systems used in complex logistics operations, including Amazon facilities. Skills and technologies: Java, JSP, GWT, Android, Oracle, PL/SQL, WMS. Company website: https://www.senior.com.br/.",
						Map.of(
								"title", "Senior Software Engineer at Senior Sistemas",
								"category", "experience",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Ricardo Mello's professional experience at Simus. Role: Java Developer. Period: Sep 2010 — Dec 2011. Location: Brazil. Responsibilities and achievements: Maintained and evolved the SUPERUS ERP with Java services, Delphi screens, and Oracle PL/SQL solutions. Skills and technologies: Java, Delphi, Oracle PL/SQL, Vaadin, ERP. Company website: https://simus.com.br/.",
						Map.of(
								"title", "Java Developer at Simus",
								"category", "experience",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Event: MongoDB Build & Learn Series - MongoDB Overview. 1st Session Date: September 15, 2026. Location: Online. Format/type: Workshop. Status: upcoming.",
						Map.of(
								"title", "MongoDB Build & Learn Series - MongoDB Overview",
								"category", "event",
								"createdAt", "2026-09-15"
						)
				),
				new Document(
						"Event: MongoDB Build & Learn Series - Data Modeling for MongoDB. 2nd Session Date: September 22, 2026. Location: Online. Format/type: Workshop. Status: upcoming.",
						Map.of(
								"title", "MongoDB Build & Learn Series - Data Modeling for MongoDB",
								"category", "event",
								"createdAt", "2026-09-22"
						)
				),
				new Document(
						"Event: MongoDB Build & Learn Series - Vector Search Fundamentals. 3rd Session (SA) Date: October 6, 2026. Location: Online. Format/type: Workshop. Status: upcoming.",
						Map.of(
								"title", "MongoDB Build & Learn Series - Vector Search Fundamentals",
								"category", "event",
								"createdAt", "2026-10-06"
						)
				),
				new Document(
						"Event: MongoDB Build & Learn Series - AI Agents with MongoDB. 4th Session Date: November 3, 2026. Location: Online. Format/type: Workshop. Status: upcoming.",
						Map.of(
								"title", "MongoDB Build & Learn Series - AI Agents with MongoDB",
								"category", "event",
								"createdAt", "2026-11-03"
						)
				),
				new Document(
						"Event: MongoDB Build & Learn Series - Building Applications with MongoDB and Agentic Coding Tools. 5th Session Date: November 17, 2026. Location: Online. Format/type: Workshop. Status: upcoming.",
						Map.of(
								"title", "MongoDB Build & Learn Series - Building Applications with MongoDB and Agentic Coding Tools",
								"category", "event",
								"createdAt", "2026-11-17"
						)
				),
				new Document(
						"Event: .local São Paulo. Talk/topic: Fundamentals of Data Transformation with the MongoDB Aggregation Framework and Java.. Hands-on workshop covering data transformation and aggregation pipelines using MongoDB and the Java Driver. Date: July 29, 2026. Location: São Paulo, Brazil. Format/type: .Local. Status: past event. Highlight: .local São Paulo. A hands-on workshop at MongoDB .local São Paulo focused on transforming and processing data with the MongoDB Aggregation Framework using the official Java Driver. During [MongoDB .local São Paulo](https://www.mongodb.com/pt-br/events/mongodb-local/sao-paulo), we explored how developers can use the MongoDB Aggregation Framework to filter, sort, group, reshape, and transform data directly inside the database. Using the official MongoDB Java Driver, we built aggregation pipelines with stages such as $match, $project, $sort, $limit, $unwind, $group, $lookup, and $set. The workshop combined practical Java examples, hands-on exercises, and aggregation best practices to help developers move from basic queries to more advanced data-processing scenarios with MongoDB. Event page: https://ricardohsmello.com/events/mongodb-local-sao-paulo-2026.",
						Map.of(
								"title", ".local São Paulo",
								"category", "event",
								"createdAt", "2026-07-29"
						)
				),
				new Document(
						"Event: Banco do Brasil - Dev Day. Talk/topic: Exploring CRUD operations and the Aggregation Framework using the MongoDB Java Driver.. Hands-on session covering MongoDB CRUD operations and aggregation pipelines with Java Date: Jun 2026. Location: Brasília, Brazil. Format/type: Dev Day. Status: past event. Highlight: Dev Day. A hands-on technical session at Banco do Brasil focused on using MongoDB with Java, from basic CRUD operations to more advanced data processing with the Aggregation Framework. In this session at [Banco do Brasil](https://www.bb.com.br/), we explored how to use the official MongoDB Java Driver to perform CRUD operations and interact with MongoDB from Java applications. We also covered the MongoDB Aggregation Framework, showing how developers can build pipelines to filter, $project, $sort, $group, and transform data directly inside the database using Java. The session was focused on practical examples, helping Java developers understand how to move from basic database operations to more advanced data processing scenarios with MongoDB. Event page: https://ricardohsmello.com/events/bb-devday-2026.",
						Map.of(
								"title", "Banco do Brasil - Dev Day",
								"category", "event",
								"createdAt", "2026-06-01"
						)
				),
				new Document(
						"Event: Aggregation Framework with Java. Talk/topic: Master the aggregation pipeline framework to perform complex data filtering, grouping, and transformations using Java.. Hands-on workshop exploring the MongoDB Aggregation Framework with Java Date: May 2026. Location: Remote, Global. Format/type: Workshop. Status: past event. Highlight: Workshop. The transition from traditional, rigid database schemas to AI-powered application development requires a new set of skills and a flexible architectural foundation. In this workshop focused on the MongoDB Aggregation Framework, I explore how to use the MongoDB Java Sync Driver to build powerful aggregation pipelines for complex data processing scenarios. Throughout the hands-on examples, developers learn how to work with stages such as `$match`, `$project`, `$sort`, `$group`, `$set`, and more using Java. This episode is especially useful for developers who want to move beyond basic CRUD operations and understand how MongoDB can perform advanced filtering, grouping, reshaping, and analytics directly inside the database. It also provides a strong foundation for modern application architectures and AI-related workloads that rely on efficient data transformations. You can access the online development lab on: [Instruqt](https://play.instruqt.com/mongo-devrel/invite/cscphzbiq29p). You can watch the full episode on [YouTube](https://www.youtube.com/watch?v=resZ9jzTvPg). Event page: https://ricardohsmello.com/events/aggregation-framework-java-may-2026.",
						Map.of(
								"title", "Aggregation Framework with Java",
								"category", "event",
								"createdAt", "2026-05-01"
						)
				),
				new Document(
						"Event: MongoDB CRUD Operations in Java. Talk/topic: Learn to perform create, read, update, and delete (CRUD) operations by translating SQL concepts into the Java-based MongoDB Query API.. Hands-on webinar exploring MongoDB CRUD operations with Java Date: May 2026. Location: Remote, Global. Format/type: Workshop. Status: past event. Highlight: Workshop. The transition from traditional, rigid database schemas to AI-powered application development requires a new set of skills and a flexible architectural foundation. In this episode focused on CRUD operations, I explore how to use the MongoDB Java Sync Driver to perform core database operations such as finding, inserting, updating, and deleting documents. Throughout the hands-on examples, developers will learn how to build queries, apply projections, sort and limit results, work with array fields, and understand concepts like upsert and bulk operations using Java. This episode is especially useful for developers who are starting their journey with MongoDB and want to understand how traditional database concepts translate into the document model using Java. It is also a great foundation for developers preparing to work with more advanced topics such as aggregation pipelines, schema design, and AI-powered applications with MongoDB. You can access the online development lab on:[Instruqt](https://play.instruqt.com/mongo-devrel/invite/asmaxisdfimf). You can watch the full episode on [YouTube](https://www.youtube.com/watch?v=dPyI_KH-xq0). Event page: https://ricardohsmello.com/events/crudoperations-java-may-2026.",
						Map.of(
								"title", "MongoDB CRUD Operations in Java",
								"category", "event",
								"createdAt", "2026-05-01"
						)
				),
				new Document(
						"Event: Talk at DevStandup. Talk/topic: Real-Time Fraud Detection in Java with Kafka, Streams & Vector Similarity. Spoke on DevStandup about Vector Search and Kafka Streams Date: Apr 2026. Location: Remote, Global. Format/type: Community. Status: past event. Highlight: Youtube Community. Episode #27 of DevStandup features Ricardo Mello, Senior Developer Advocate at MongoDB, breaking down how to build a real-time fraud detection system in Java—without training your own ML model. This session focuses on what most teams get wrong: overengineering fraud systems when simple, fast pipelines and good heuristics already solve 80% of the problem. In 'Real-Time Fraud Detection in Java with Kafka, Streams & Vector Similarity', Ricardo shows how to ship a system that actually works under pressure. This episode is for developers who want working systems, not theoretical architectures that never make it to production. The full project code — including the Kafka Streams pipeline and MongoDB Vector Search integration — is available on [GitHub](https://github.com/ricardohsmello/fraud-detection-streams-vector). You can watch the full episode on [YouTube](https://www.youtube.com/watch?v=erGc9YkQ0i8). Event page: https://ricardohsmello.com/events/talkdevstandup-vector-2026.",
						Map.of(
								"title", "Talk at DevStandup",
								"category", "event",
								"createdAt", "2026-04-01"
						)
				),
				new Document(
						"Event: Regional MongoDB. Talk/topic: Data Modeling & Atlas Search. Spoke on data modeling, Atlas Search and Schema design pattern. Date: Mar 2026. Location: Brasilia, Brazil. Format/type: Dev Day. Status: past event. Highlight: 100+ developers attended. The MongoDB Developer Day in Brasília was a full-day, hands-on event designed for developers who want to learn how to build modern applications using MongoDB. Throughout the event, participants explored key concepts such as document data modeling, schema design patterns, and advanced querying techniques. The sessions were highly practical, allowing attendees to apply what they were learning in real time. We also introduced modern capabilities like search and other advanced MongoDB features, helping developers understand how to design scalable and flexible applications. The event brought together over 100 developers from the region, creating a strong environment for learning, collaboration, and knowledge sharing. By the end of the day, participants had the opportunity to validate their knowledge by earning skill badges, reinforcing the hands- on nature of the experience. MongoDB Developer Day is focused on empowering developers with practical skills, real-world patterns, and a deeper understanding of how to work with data in modern architectures. Beyond the technical sessions, one of the most valuable outcomes of the event was the connections we built with the local community. We had the chance to engage with developers, understand their challenges, and see a strong interest in growing with MongoDB. These conversations opened the door for new opportunities in the region, including potential MongoDB User Groups and future initiatives. This is just the beginning of expanding our presence and supporting the community in Brasília. Event page: https://ricardohsmello.com/events/regional-devday-brasilia-2026.",
						Map.of(
								"title", "Regional MongoDB",
								"category", "event",
								"createdAt", "2026-03-01"
						)
				),
				new Document(
						"Event: Dev/nexus. Talk/topic: Real-Time Fraud Detection. Spoke on real-time fraud detection using Kafka Streams, Spring Boot, and MongoDB. Also hosted a mentorship session on careers in Developer Relations. Date: Mar 2026. Location: Atlanta, USA. Format/type: Conference. Status: past event. Highlight: First tech talk delivered in English. DevNexus is one of the largest Java conferences in the United States, bringing together thousands of developers to discuss modern software architecture, cloud technologies, and the future of the Java ecosystem. In 2026, I had the opportunity to speak at DevNexus for the first time, and it was also my first time delivering a full technical session in English. During the session, I presented a real-world architecture for real-time fraud detection, demonstrating how modern event-driven systems can process transactions and make decisions in milliseconds. The talk explored how technologies like Kafka Streams, Spring Boot, and MongoDB can be combined to build resilient systems capable of handling high-throughput data streams while maintaining low latency. In addition to the technical talk, I also hosted a mentorship session during the conference focused on career growth in the developer ecosystem, speaking with developers interested in understanding the transition from software engineering to Developer Relations. Presenting at DevNexus was an important milestone in my journey as a Developer Advocate and a great opportunity to share technical knowledge with the global Java community. Event page: https://ricardohsmello.com/events/devnexus-2026.",
						Map.of(
								"title", "Dev/nexus",
								"category", "event",
								"createdAt", "2026-03-01"
						)
				),
				new Document(
						"Event: CITI Bank - MongoDB Day. Talk/topic: MongoDB Developer Day. MongoDB Developer Day with hands-on labs and a full Design Review with multiple engineering teams at Citi. Date: Feb 2026. Location: São Paulo, Brazil. Format/type: Enterprise. Status: past event. Highlight: Full Design Review with multiple engineering teams. Last week I had the opportunity to deliver another MongoDB Developer Day in São Paulo, hosted by Citi. Developer Day is a hands-on enablement program where we go deep into topics like data modeling, architecture decisions, and advanced capabilities in MongoDB. This time, we also ran a full Design Review with several engineering teams, discussing real challenges and how MongoDB can support their architecture moving forward. Finally, I would like to thank all the participants of the Developer Day and my MongoDB colleagues for helping make this such a great event. Event page: https://ricardohsmello.com/events/citibank-2026.",
						Map.of(
								"title", "CITI Bank - MongoDB Day",
								"category", "event",
								"createdAt", "2025-02-26"
						)
				),
				new Document(
						"Event: .local 2025. Talk/topic: Data Modeling & Schema Design Pattern. Two sessions on MongoDB data modeling and Design Pattern. Date: July 2025. Location: São Paulo, Brazil. Format/type: Enterprise. Status: past event. Highlight: 119 skill badges earned by attendees. For the second year in a row, I had the pleasure of being on stage at .local São Paulo 2025, the largest MongoDB gathering in Brazil. This time, I led a hands-on Data Modeling session, where we rolled up our sleeves and put theory into practice. It was amazing to see 119 participants walk away with an official MongoDB Skill Badge, proof of their dedication and curiosity. What really stood out was the energy in the room, people excited to dive deeper, ask questions, and bring their ideas to life with MongoDB. I also had the chance to finally meet Veronica in person, my amazing teammate at MongoDB. She leads our global community programs, including Champions, Creators, and so many other initiatives that help connect and empower developers around the world. After years of working together remotely, it was truly special to finally share the same space! Event page: https://ricardohsmello.com/events/local-2025.",
						Map.of(
								"title", ".local 2025",
								"category", "event",
								"createdAt", "2025-07-01"
						)
				),
				new Document(
						"Event: LATAM MongoDB Community. Talk/topic: Data Modeling & Vector Search. Two sessions on MongoDB data modeling and Vector Search. Date: July 2025. Location: São Paulo, Brazil. Format/type: Enterprise. Status: past event. Highlight: MongoDB Champions & Creators community. I recently had the opportunity to connect with the amazing MongoDB Community, including Champions, Creators, and many talented developers. I delivered a hands-on training session covering Vector Search: From Beginner to Pro and SQL to MongoDB Query API. recently had the opportunity to connect with the amazing MongoDB Community, including Champions, Creators, and many talented developers. I delivered a hands-on training session covering Vector Search: From Beginner to Pro and SQL to MongoDB Query API. Beyond the technical deep dives, it was also a great moment for networking, sharing experiences, and learning from each other. Always grateful to be part of this inspiring global community. Event page: https://ricardohsmello.com/events/latam-community-2025.",
						Map.of(
								"title", "LATAM MongoDB Community",
								"category", "event",
								"createdAt", "2025-07-01"
						)
				),
				new Document(
						"Event: Developer Day – C6 Bank. Talk/topic: Data Modeling & Schema Design Patterns / Anti-patterns in MongoDB. Two sessions on MongoDB data modeling, schema design patterns, and anti-patterns to avoid in document databases. Date: Mar 2025. Location: São Paulo, Brazil. Format/type: Enterprise. Status: past event. Highlight: 2 sessions for C6 Bank engineering teams. I had the opportunity to participate in Developer Day at C6 Bank, an internal event dedicated to learning, innovation, and collaboration among engineering teams. During the event, I delivered two sessions on MongoDB, one focused on data modeling and schema design patterns, and another exploring anti-patterns to avoid when working with document databases. The audience was highly engaged, bringing thoughtful questions and real-world challenges to the conversation. It was a great moment to share knowledge, connect with developers, and explore how MongoDB can empower modern application development. Special thanks to the C6 team for making this experience possible. I'm looking forward to continuing the conversation and supporting the tech community on their journey with MongoDB. Event page: https://ricardohsmello.com/events/devday-c6-2025.",
						Map.of(
								"title", "Developer Day – C6 Bank",
								"category", "event",
								"createdAt", "2025-03-01"
						)
				),
				new Document(
						"Event: Dev/nexus. Talk/topic: MongoDB at the Booth. Represented MongoDB at the booth, talking about Spring, Spring Boot, and Spring Data integrations with the global Java community. Date: Mar 4–6, 2025. Location: Atlanta, USA. Format/type: Conference. Status: past event. Highlight: MongoDB booth at the largest Java conf in the US. DevNexus is one of the largest Java conferences, bringing developers together to share knowledge, explore new technologies, and connect with the global Java community. During the event, we set up a MongoDB booth where we had many great conversations with developers. We talked a lot about how to integrate MongoDB with Java applications, especially using Spring, Spring Boot, and Spring Data to build modern and scalable systems. Beyond the technical conversations, DevNexus was also a great opportunity to make new connections and friendships with developers from all over the world. I had the chance to meet amazing Java developers and Champions, exchange ideas, and discuss the latest trends shaping modern Java development. Event page: https://ricardohsmello.com/events/devnexus-2025.",
						Map.of(
								"title", "Dev/nexus",
								"category", "event",
								"createdAt", "2025-03-04"
						)
				),
				new Document(
						"Event: Developer Day - Gen AI. Talk/topic: GenAI with MongoDB. MongoDB Developer Day in São Paulo focused on GenAI, with hands-on labs for the local developer community. Date: Nov 2025. Location: São Paulo, Brazil. Format/type: Dev Day. Status: past event. Highlight: Hands-on GenAI labs with the local dev community. We recently hosted a MongoDB Developer Day in São Paulo focused on GenAI, and it was a great success with the developer community. It was amazing to see developers engaging with the content, asking great questions, and experimenting with the technology during the hands-on labs. Thanks to everyone who joined us and helped make the event such a great experience. Event page: https://ricardohsmello.com/events/devday-genai-2025.",
						Map.of(
								"title", "Developer Day - Gen AI",
								"category", "event",
								"createdAt", "2025-11-01"
						)
				),
				new Document(
						"Event: Spring I/O - Barcelona, Spain. Represented MongoDB at the MongoDB booth, connecting with the global Spring community and helping developers with questions about MongoDB and its integrations with Spring. Date: May 2025. Location: Barcelona, Spain. Format/type: Conference. Status: past event. Highlight: Represented MongoDB at the booth and connected with the Spring ecosystem. In 2025, I attended Spring I/O in Barcelona, one of the key events for the global Spring community. During the conference, I represented MongoDB at our booth, where I had the opportunity to talk with developers from around the world, answer questions about MongoDB, and discuss integrations with Spring, Spring Boot, and Spring Data MongoDB. Many of the conversations focused on how developers can use MongoDB in modern Java and Kotlin applications, including topics such as data access with Spring Data MongoDB, application architecture, and newer capabilities like Vector Search. Beyond the technical conversations at the booth, the event was also a great opportunity to reconnect with friends, meet members of the Spring ecosystem, learn from the community, and exchange ideas for new technical content. It's always motivating to be surrounded by developers who are passionate about Java, Spring, and building modern applications. Event page: https://ricardohsmello.com/events/springio-2025.",
						Map.of(
								"title", "Spring I/O - Barcelona, Spain",
								"category", "event",
								"createdAt", "2025-05-01"
						)
				),
				new Document(
						"Event: Regional MongoDB. Talk/topic: Data Modeling & Schema Design Patterns. First MongoDB × AWS Developer Day in São Paulo — two sessions on data modeling and schema design patterns. Date: May 2025. Location: São Paulo, Brazil. Format/type: Dev Day. Status: past event. Highlight: First MongoDB × AWS Developer Day in São Paulo. MongoDB and AWS teamed up for the first edition of Developer Day in São Paulo! It was an exclusive event for developers, architects, and their teams to explore the power of MongoDB and learn how to build modern, flexible, and scalable applications. I had the chance to speak at this event, delivering two sessions focused on data modeling and schema design patterns. It was a highlight in an intense May, filled with amazing opportunities to share knowledge. A huge thank you to my friends and teammates who helped make this event happen. It was awesome to be part of such an engaged and passionate community, learning and sharing ideas together. Event page: https://ricardohsmello.com/events/devday-regional-sp-2025.",
						Map.of(
								"title", "Regional MongoDB",
								"category", "event",
								"createdAt", "2025-05-01"
						)
				),
				new Document(
						"Event: 10th Engineering and Technology Week. Talk/topic: MongoDB Fundamentals & NoSQL. Workshop on MongoDB fundamentals, NoSQL vs relational databases, and official CRUD certification for university students. Date: Apr 2025. Location: Ribeirão Preto, Brazil. Format/type: Workshop. Status: past event. Highlight: CRUD certification earned · 2nd year in a row. For the second year in a row, I was honored to be invited to speak about MongoDB at the Engineering and Technology Week at the University of Ribeirão Preto. It was amazing to see how engaged and curious the students were! During my talk, I explained the basics of databases, starting with the relational model. From there, I transitioned to NoSQL databases, making comparisons around scalability, flexibility, and real-world use cases. To wrap things up, the students earned their official CRUD certification from MongoDB. Big thanks to the university and to Professor Edilson Caritá for the invitation — and of course, to all the students who participated, shared their ideas, and took home some awesome swags. Event page: https://ricardohsmello.com/events/10th-eng-2025.",
						Map.of(
								"title", "10th Engineering and Technology Week",
								"category", "event",
								"createdAt", "2025-04-01"
						)
				),
				new Document(
						"Event: NoSQL Connect. Talk/topic: Queryable Encryption with Java. Session on MongoDB Queryable Encryption with Java, plus a co-presented talk on data modeling and MongoDB Atlas with Leandro Domingues. Date: Apr 2025. Location: São Paulo, Brazil. Format/type: Community. Status: past event. Highlight: Oracle Java Community. I had the pleasure of delivering a session on MongoDB Queryable Encryption at Casa Oracle — an event hosted by the Oracle community. I shared practical examples of how to implement it using Java, emphasizing how developers can secure sensitive data while still enabling expressive queries. I also teamed up with Leandro Domingues for a session focused on the fundamentals of data modeling and exploring MongoDB Atlas. It was a great opportunity to connect with the community, learn from others, and exchange valuable insights. I'm grateful for the opportunity to be part of such a vibrant event, and I look forward to more moments like this to keep sharing, learning, and growing together with the community! Event page: https://ricardohsmello.com/events/nosql-forum.",
						Map.of(
								"title", "NoSQL Connect",
								"category", "event",
								"createdAt", "2025-04-01"
						)
				),
				new Document(
						"Event: MongoDB .local São Paulo. Talk/topic: MongoDB Relational Migrator. Session on MongoDB Relational Migrator for 80+ participants, plus hands-on RAG application lab at this global MongoDB .local event. Date: Oct 2024. Location: São Paulo, Brazil. Format/type: .Local. Status: past event. Highlight: 80+ participants · Global .local event in Brazil. I had the opportunity to speak at .local São Paulo, a global MongoDB event hosted in Brazil. During the event, I presented a session on MongoDB Relational Migrator for more than 80 participants. In the talk, I explored common challenges around database migration and data modeling, and demonstrated how to migrate a relational database with thousands of records, convert stored procedures into Java code, and generate MongoDB queries. I also supported a hands-on lab focused on building RAG applications, helping participants explore modern AI-driven application patterns with MongoDB. It was a great opportunity to connect with the MongoDB community, including Champions and Creators, while exchanging ideas and networking with developers interested in modern data architectures. Event page: https://ricardohsmello.com/events/mongodb-local-sao-paulo-2024.",
						Map.of(
								"title", "MongoDB .local São Paulo",
								"category", "event",
								"createdAt", "2024-10-01"
						)
				),
				new Document(
						"Event: 9th Engineering and Technology Week – Unaerp. Talk/topic: MongoDB Workshop. Two-day MongoDB workshop on fundamentals and data modeling for Engineering and Computer Science students. Date: Sep 2024. Location: Ribeirão Preto, Brazil. Format/type: Workshop. Status: past event. Highlight: 2-day workshop · Engineering & CS students. I had the opportunity to deliver a two-day MongoDB workshop during the 9th Engineering and Technology Week at the University of Ribeirão Preto (Unaerp). The mini-course was designed for students from Engineering and Computer Science programs. Over the two days, we explored MongoDB fundamentals, data modeling concepts, and practical examples of building modern applications with document databases. It was a very rewarding experience to teach MongoDB and see the enthusiasm and curiosity of students interested in learning modern data technologies. Event page: https://ricardohsmello.com/events/unaerp-engineering-week-2024.",
						Map.of(
								"title", "9th Engineering and Technology Week – Unaerp",
								"category", "event",
								"createdAt", "2024-09-01"
						)
				),
				new Document(
						"Event: Technology Meetup – UNIP. Talk/topic: MongoDB: Fundamentals to Real-World. MongoDB talk for 500+ participants — from fundamentals to real-world use cases, at Universidade Paulista. Date: Jun 2024. Location: São Paulo, Brazil. Format/type: Meetup. Status: past event. Highlight: 500+ attendees at Universidade Paulista. I had the pleasure of speaking about MongoDB at the Technology Meetup hosted by Universidade Paulista. The event brought together more than 500 participants, including students, alumni, professors, and technology enthusiasts. It was an incredible experience to present MongoDB to such a large audience. My goal was simple: help those who didn't know MongoDB discover it, and give those who already knew it a deeper appreciation of what it can do. Judging by the conversations and messages after the talk, it seems we achieved that goal. It was a fantastic experience sharing knowledge with so many people passionate about technology. Event page: https://ricardohsmello.com/events/unip-technology-meetup-2024.",
						Map.of(
								"title", "Technology Meetup – UNIP",
								"category", "event",
								"createdAt", "2024-06-01"
						)
				),
				new Document(
						"Event: C6 Bank – MongoDB Day. Talk/topic: Real-World MongoDB with PIX. Presented a real-world MongoDB use case with C6 Bank's PIX team, exploring scalable financial application patterns. Date: Feb 2024. Location: São Paulo, Brazil. Format/type: Dev Day. Status: past event. Highlight: Live PIX use case with C6 Bank's engineering team. We recently hosted a MongoDB Day at C6 Bank and it was a great experience. I had the opportunity to present a real-world use case using MongoDB with the PIX team, showing how the platform can support modern, scalable financial applications. It was great to see so many people engaged in the discussions and interested in learning more about how MongoDB can be used to solve real technical challenges. Events like this are a great opportunity to share knowledge and strengthen the local developer community. Event page: https://ricardohsmello.com/events/c6-bank-dev-day-2024.",
						Map.of(
								"title", "C6 Bank – MongoDB Day",
								"category", "event",
								"createdAt", "2024-02-01"
						)
				),
				new Document(
						"Article: Building a Kotlin Application with Quarkus and MongoDB: A Step-by-Step Guide. Kotlin, Quarkus, and MongoDB — do these three technologies work well together? Type: Tutorial. Published on Dev.to in 2026-06. Topics: Kotlin, Quarkus, Aggregation Framework, MongoDB, Query. Featured on the website: yes. Link: https://dev.to/mongodb/building-a-kotlin-application-with-quarkus-and-mongodb-a-step-by-step-guide-231l.",
						Map.of(
								"title", "Building a Kotlin Application with Quarkus and MongoDB: A Step-by-Step Guide",
								"category", "article",
								"createdAt", "2026-06-01"
						)
				),
				new Document(
						"Article: Real-Time Fraud Detection in Java with Kafka Streams and Vector Similarity. This content is based on a talk that my colleague Tim Kelly and I presented at DevNexus 2026 in Atlanta, one of the largest Java conferences in the world. Type: Article. Published on Dev.to in 2026-04. Topics: Java, Vector Search, Kafka, Streams, Real-time, Processing. Featured on the website: no. Link: https://dev.to/mongodb/real-time-fraud-detection-in-java-with-kafka-streams-and-vector-similarity-n2a.",
						Map.of(
								"title", "Real-Time Fraud Detection in Java with Kafka Streams and Vector Similarity",
								"category", "article",
								"createdAt", "2026-04-01"
						)
				),
				new Document(
						"Article: Discover Your Ideal Airbnb: Implementing a Spring Boot & MongoDB Search With Kotlin Sync Driver. Build an application in Kotlin that utilizes full-text search in a database containing thousands of Airbnb listings to find the perfect accommodation. Type: Tutorial. Published on Foojay.io in 2026-04. Topics: Kotlin, Search. Featured on the website: no. Link: https://foojay.io/today/discover-your-ideal-airbnb-implementing-a-spring-boot-mongodb-search-with-kotlin-sync-driver/.",
						Map.of(
								"title", "Discover Your Ideal Airbnb: Implementing a Spring Boot & MongoDB Search With Kotlin Sync Driver",
								"category", "article",
								"createdAt", "2026-04-01"
						)
				),
				new Document(
						"Article: MongoDB Sharding: What to Know Before You Shard. Sharding as a horizontal scaling strategy, understanding a sharded cluster architecture, shards, config servers, mongos, and distributing data. Type: Article. Published on Foojay.io in 2026-02. Topics: MongoDB, Databases. Featured on the website: no. Link: https://foojay.io/today/mongodb-sharding-what-to-know-before-you-shard/.",
						Map.of(
								"title", "MongoDB Sharding: What to Know Before You Shard",
								"category", "article",
								"createdAt", "2026-02-01"
						)
				),
				new Document(
						"Article: MongoDB 8.0 Migration Guide: What You Need to Know Before Upgrading. What's new in version 8.0, general changes, Queryable Encryption, Express query stages, query shape and query settings, compatibility and more. Type: Guide. Published on Foojay.io in 2026-02. Topics: MongoDB, Databases. Featured on the website: no. Link: https://foojay.io/today/mongodb-8-0-migration-guide-what-you-need-to-know-before-upgrading/.",
						Map.of(
								"title", "MongoDB 8.0 Migration Guide: What You Need to Know Before Upgrading",
								"category", "article",
								"createdAt", "2026-02-01"
						)
				),
				new Document(
						"Article: How to Build Image Similarity Search With Atlas Functions and Triggers. Have you ever imagined taking a photo of yourself, comparing it against a database full of celebrities, and instantly finding out who you look most alike? Type: Tutorial. Published on Dev.to in 2026-01. Topics: Function, Vector Search, Triggers, MongoDB. Featured on the website: no. Link: https://dev.to/mongodb/how-to-build-image-similarity-search-with-atlas-functions-and-triggers-m50.",
						Map.of(
								"title", "How to Build Image Similarity Search With Atlas Functions and Triggers",
								"category", "article",
								"createdAt", "2026-01-01"
						)
				),
				new Document(
						"Article: Modeling Relationships With Hibernate ORM and MongoDB. Prerequisites, one-to-many relationships, avoiding embedded inside books, and moving reviews to a separate collection. Type: Tutorial. Published on Foojay.io in 2025-11. Topics: Hibernate, Java, Databases. Featured on the website: no. Link: https://foojay.io/today/modeling-relationships-with-hibernate-orm-and-mongodb/.",
						Map.of(
								"title", "Modeling Relationships With Hibernate ORM and MongoDB",
								"category", "article",
								"createdAt", "2025-11-01"
						)
				),
				new Document(
						"Article: Getting Started With Hibernate ORM and MongoDB. How MongoDB fits in, prerequisites, tag your Atlas cluster, project overview, setting up the project, configure Hibernate, the Book entity. Type: Tutorial. Published on Foojay.io in 2025-11. Topics: Hibernate, Java. Featured on the website: no. Link: https://foojay.io/today/getting-started-with-hibernate-orm-and-mongodb/.",
						Map.of(
								"title", "Getting Started With Hibernate ORM and MongoDB",
								"category", "article",
								"createdAt", "2025-11-01"
						)
				),
				new Document(
						"Article: Beyond Keywords: Hybrid Search with Atlas and Vector Search (Part 3). One search might not be enough. Merging the best of both worlds: prerequisites, project setup, full-text search, implementing the full-text index. Type: Part 3. Published on Foojay.io in 2025-11. Topics: Atlas Search, AI. Featured on the website: no. Link: https://foojay.io/today/beyond-keywords-hybrid-search-with-atlas-and-vector-search-part-3/.",
						Map.of(
								"title", "Beyond Keywords: Hybrid Search with Atlas and Vector Search (Part 3)",
								"category", "article",
								"createdAt", "2025-11-01"
						)
				),
				new Document(
						"Article: Beyond Keywords: Optimizing Vector Search With Filters and Caching (Part 2). Adding filters from story to code, post-filter in MovieService, pre-filter approach, refining the search with extra filters and applying toCriteria. Type: Part 2. Published on Foojay.io in 2025-10. Topics: Vector Search, Java, AI. Featured on the website: no. Link: https://foojay.io/today/beyond-keywords-optimizing-vector-search-with-filters-and-caching-part-2/.",
						Map.of(
								"title", "Beyond Keywords: Optimizing Vector Search With Filters and Caching (Part 2)",
								"category", "article",
								"createdAt", "2025-10-01"
						)
				),
				new Document(
						"Article: Beyond Keywords: Implementing Semantic Search in Java With Spring Data (Part 1). The magic behind vector search, prerequisites, set up embeddings, work with embeddings, and MongoDB Atlas Vector Search. Type: Part 1. Published on Foojay.io in 2025-10. Topics: Vector Search, Java, Spring. Featured on the website: no. Link: https://foojay.io/today/beyond-keywords-implementing-semantic-search-in-java-with-spring-data-part-1/.",
						Map.of(
								"title", "Beyond Keywords: Implementing Semantic Search in Java With Spring Data (Part 1)",
								"category", "article",
								"createdAt", "2025-10-01"
						)
				),
				new Document(
						"Article: Queryable Encryption With Spring Data MongoDB: How to Query Encrypted Fields. Why Queryable Encryption, a quick look at Spring MongoDB integration, defining encrypted fields, configuration, and dependencies. Type: Tutorial. Published on Foojay.io in 2025-09. Topics: MongoDB, Spring, Security. Featured on the website: no. Link: https://foojay.io/today/queryable-encryption-with-spring-data-mongodb-how-to-query-encrypted-fields/.",
						Map.of(
								"title", "Queryable Encryption With Spring Data MongoDB: How to Query Encrypted Fields",
								"category", "article",
								"createdAt", "2025-09-01"
						)
				),
				new Document(
						"Article: Why Mirroring Production in Dev Helps You Avoid Costly Mistakes. A realistic aggregation scenario, writing the test, testing on M0, the hidden risk, and taking it to production with the same queries on different environments. Type: Article. Published on Foojay.io in 2025-07. Topics: MongoDB, Best Practices, Migration. Featured on the website: no. Link: https://foojay.io/today/why-mirroring-production-in-dev-helps-you-avoid-costly-mistakes/.",
						Map.of(
								"title", "Why Mirroring Production in Dev Helps You Avoid Costly Mistakes",
								"category", "article",
								"createdAt", "2025-07-01"
						)
				),
				new Document(
						"Article: Clean and Modular Java: A Hexagonal Architecture Approach. One of the discussions that always leaves me with both doubts and excitement is the one about hexagonal architecture, diving deeper into modular design. Type: Article. Published on Foojay.io in 2025-06. Topics: Java, Architecture. Featured on the website: no. Link: https://foojay.io/today/clean-and-modular-java-a-hexagonal-architecture-approach/.",
						Map.of(
								"title", "Clean and Modular Java: A Hexagonal Architecture Approach",
								"category", "article",
								"createdAt", "2025-06-01"
						)
				),
				new Document(
						"Article: MongoDB Aggregation Framework: A Beginner's Guide. Finding exactly the data we need isn't always simple. Learn how to filter, group, and perform calculations using MongoDB's Aggregation Framework. Type: Tutorial. Published on Foojay.io in 2025-06. Topics: Java, Aggregation Framework, MongoDB. Featured on the website: no. Link: https://foojay.io/today/mongodb-aggregation-framework-a-beginners-guide/.",
						Map.of(
								"title", "MongoDB Aggregation Framework: A Beginner's Guide",
								"category", "article",
								"createdAt", "2025-06-01"
						)
				),
				new Document(
						"Article: Spring Data Unlocked: Performance Optimization Techniques With MongoDB. Learn how to optimize a Spring Data MongoDB application through indexes and read preferences. This third and final part of the series covers single field, compound, unique, and TTL/partial indexes, as well as ReadPreference strategies. Type: Part 3. Published on Dev.to in 2025-03. Topics: Java, Spring Data, MongoDB. Featured on the website: no. Link: https://dev.to/ricardohsmello/spring-data-unlocked-performance-optimization-techniques-with-mongodb-5f5.",
						Map.of(
								"title", "Spring Data Unlocked: Performance Optimization Techniques With MongoDB",
								"category", "article",
								"createdAt", "2025-03-01"
						)
				),
				new Document(
						"Article: Spring Data Unlocked: Advanced Queries With MongoDB. Explore Spring Data's capabilities by creating advanced queries with MongoRepository and MongoTemplate. This second part of the series covers derived queries, @Query, @Update, @Aggregation, pagination, and bulk operations. Type: Part 2. Published on Dev.to in 2025-02. Topics: Java, Spring Data, MongoDB, Query. Featured on the website: no. Link: https://dev.to/ricardohsmello/spring-data-unlocked-advanced-queries-with-mongodb-4jef.",
						Map.of(
								"title", "Spring Data Unlocked: Advanced Queries With MongoDB",
								"category", "article",
								"createdAt", "2025-02-01"
						)
				),
				new Document(
						"Article: Spring Data Unlocked: Getting Started With Java and MongoDB. Learn how to integrate MongoDB with Spring Data in a simple and easy way. This first part of the series covers getting started with Spring Data, modeling your data, and using MongoRepository to perform basic CRUD operations. Type: Part 1. Published on Dev.to in 2025-01. Topics: Java, Spring Data, MongoDB. Featured on the website: no. Link: https://dev.to/ricardohsmello/spring-data-unlocked-getting-started-with-java-and-mongodb-49f6.",
						Map.of(
								"title", "Spring Data Unlocked: Getting Started With Java and MongoDB",
								"category", "article",
								"createdAt", "2025-01-01"
						)
				),
				new Document(
						"Article: Java Meets Queryable Encryption: Developing a Secure Bank Account Application. Learn how to seamlessly integrate Java with MongoDB Queryable Encryption in a fully automated way. This process allows you to leverage the advanced encryption features of MongoDB, ensuring that your data remains protected even during complex queries, without the need for manual encryption or decryption steps Type: Article. Published on Dev.to in 2024-11. Topics: Java, Encryption, Queryable Encryption. Featured on the website: no. Link: https://dev.to/ricardohsmello/java-meets-queryable-encryption-developing-a-secure-bank-account-application-40im.",
						Map.of(
								"title", "Java Meets Queryable Encryption: Developing a Secure Bank Account Application",
								"category", "article",
								"createdAt", "2024-11-01"
						)
				),
				new Document(
						"Article: Beyond Basics: Enhancing Kotlin Ktor API With Vector Search. Learn how to integrate Vector Search into your Kotlin with Ktor application using MongoDB. Type: Article. Published on Dev.to in 2024-05. Topics: Kotlin, Ktor, Vector Search. Featured on the website: no. Link: https://dev.to/ricardohsmello/test-3d5p.",
						Map.of(
								"title", "Beyond Basics: Enhancing Kotlin Ktor API With Vector Search",
								"category", "article",
								"createdAt", "2024-05-01"
						)
				),
				new Document(
						"Article: Mastering Kotlin: Creating an API With Ktor and MongoDB Atlas. Set up a Ktor project, implement CRUD operations, define API route endpoints, and run the application. Understand Kotlin's capabilities in API development. Type: Article. Published on Dev.to in 2024-04. Topics: Kotlin, Ktor, MongoDB. Featured on the website: no. Link: https://dev.to/ricardohsmello/mastering-kotlin-creating-an-api-with-ktor-and-mongodb-atlas-2dp3.",
						Map.of(
								"title", "Mastering Kotlin: Creating an API With Ktor and MongoDB Atlas",
								"category", "article",
								"createdAt", "2024-04-01"
						)
				),
				new Document(
						"Article: MongoDB Compass — Convert Text into Queries with AI-Powered Natural Language. Explore Query With Natural Language, a revolutionary feature in MongoDB Compass that lets you write queries using plain English. Type: Tutorial. Published on Medium in 2024-02. Topics: Natural Language, AI, Query, Compass. Featured on the website: no. Link: https://medium.com/itnext/mongodb-compass-convert-text-into-queries-with-ai-powered-natural-language-f708f9b54b2b.",
						Map.of(
								"title", "MongoDB Compass — Convert Text into Queries with AI-Powered Natural Language",
								"category", "article",
								"createdAt", "2024-02-01"
						)
				),
				new Document(
						"Article: Understanding MongoDB Replication: A Step-by-Step Replica Set Creation. Key facets of MongoDB Replication, including high availability and data redundancy, complemented by an accessible video walkthrough. Type: Article. Published on Medium in 2024-01. Topics: Replication, Database, MongoDB. Featured on the website: no. Link: https://itnext.io/understanding-mongodb-replication-a-step-by-step-tutorial-on-building-a-replica-set-cluster-b4267e4e2737.",
						Map.of(
								"title", "Understanding MongoDB Replication: A Step-by-Step Replica Set Creation",
								"category", "article",
								"createdAt", "2024-01-01"
						)
				),
				new Document(
						"Article: Improving MongoDB Performance | Indexes and Explain Plan in MongoDB Compass. Optimizing MongoDB queries through the strategic use of indexes and analyzing their results using the Explain Plan feature in MongoDB Compass. Type: Article. Published on Medium in 2023-10. Topics: Compass, Index, Performance. Featured on the website: no. Link: https://medium.com/predict/mongodb-compass-optimizing-performance-with-indexes-and-explain-plan-3fc15914a4a7.",
						Map.of(
								"title", "Improving MongoDB Performance | Indexes and Explain Plan in MongoDB Compass",
								"category", "article",
								"createdAt", "2023-10-01"
						)
				),
				new Document(
						"Article: MongoDB Relational Migrator | From PostgreSQL to Atlas. MongoDB's Relational Migrator feature demonstrated with a sample showing how to create and migrate a database from PostgreSQL to MongoDB. Type: Article. Published on Medium in 2023-09. Topics: Relational Migrator, Database, AI. Featured on the website: no. Link: https://itnext.io/mongodb-relational-migrator-e84c49220cef.",
						Map.of(
								"title", "MongoDB Relational Migrator | From PostgreSQL to Atlas",
								"category", "article",
								"createdAt", "2023-09-01"
						)
				),
				new Document(
						"Article: MongoDB Atlas | Charts: Crafting Powerful Visualizations using imported JSON Files. Importing a JSON file into a MongoDB Atlas cluster using MongoDB tools and building powerful charts and visualizations on top of that data. Type: Article. Published on Medium in 2023-08. Topics: Atlas, Charts, Visualization. Featured on the website: no. Link: https://itnext.io/mongodb-atlas-charts-importing-json-file-and-crafting-powerful-visualizations-915e20759a89.",
						Map.of(
								"title", "MongoDB Atlas | Charts: Crafting Powerful Visualizations using imported JSON Files",
								"category", "article",
								"createdAt", "2023-08-01"
						)
				),
				new Document(
						"Article: Exploring Data Visualization with Grafana/PostgreSQL/Docker. How to create a Grafana instance, access it, and generate graphs using data from a PostgreSQL database running in Docker. Type: Guide. Published on Medium in 2023-08. Topics: PostgreSQL, Grafana, Docker. Featured on the website: no. Link: https://itnext.io/exploring-data-visualization-with-grafana-postgresql-docker-7d9cb3fae5e9.",
						Map.of(
								"title", "Exploring Data Visualization with Grafana/PostgreSQL/Docker",
								"category", "article",
								"createdAt", "2023-08-01"
						)
				),
				new Document(
						"Article: Implementing the Observer Pattern in a Kotlin Application. An exploration of the Observer Pattern, a design pattern commonly used in software development, with a simple and practical Kotlin implementation. Type: Article. Published on Medium in 2023-04. Topics: Kotlin, Design Patterns. Featured on the website: no. Link: https://medium.com/javarevisited/implementing-the-observer-pattern-in-a-kotlin-application-381aa117e2f5.",
						Map.of(
								"title", "Implementing the Observer Pattern in a Kotlin Application",
								"category", "article",
								"createdAt", "2023-04-01"
						)
				),
				new Document(
						"Article: Deploying a Quarkus Application to AWS Elastic Beanstalk. How to deploy a Quarkus application to Elastic Beanstalk and a closer look at some of the key benefits and best practices for using these two technologies together. Type: Article. Published on Medium in 2023-03. Topics: AWS, Beanstalk, Quarkus. Featured on the website: no. Link: https://itnext.io/deploying-a-quarkus-application-to-aws-elastic-beanstalk-73c7a1962a32.",
						Map.of(
								"title", "Deploying a Quarkus Application to AWS Elastic Beanstalk",
								"category", "article",
								"createdAt", "2023-03-01"
						)
				),
				new Document(
						"Article: Quarkus + Angular with Keycloak — Pt3 Final. The final part of building our daily-quotes application with Quarkus, Angular, and Keycloak authentication. Type: Part 3. Published on Medium in 2023-03. Topics: Angular, Keycloak, Quarkus. Featured on the website: no. Link: https://itnext.io/quarkus-angular-secured-with-keycloak-pt3-44a766886a66.",
						Map.of(
								"title", "Quarkus + Angular with Keycloak — Pt3 Final",
								"category", "article",
								"createdAt", "2023-03-01"
						)
				),
				new Document(
						"Article: Quarkus + Angular with Keycloak — Pt2. Continuing the development of our daily-quotes application with Quarkus, Angular and Keycloak. Type: Part 2. Published on Medium in 2023-03. Topics: Angular, Keycloak, Quarkus. Featured on the website: no. Link: https://itnext.io/quarkus-with-angular-secured-with-keycloak-pt2-e8c1fdf7fa75.",
						Map.of(
								"title", "Quarkus + Angular with Keycloak — Pt2",
								"category", "article",
								"createdAt", "2023-03-01"
						)
				),
				new Document(
						"Article: Quarkus + Angular with Keycloak — Pt1. Delving into the authentication mechanism of Keycloak and the creation of a daily-quotes application using Angular + Quarkus, divided into three parts. Type: Part 1. Published on Medium in 2023-03. Topics: Angular, Keycloak, Quarkus. Featured on the website: no. Link: https://itnext.io/quarkus-with-angular-secured-with-keycloak-pt1-d1c00a4923b8.",
						Map.of(
								"title", "Quarkus + Angular with Keycloak — Pt1",
								"category", "article",
								"createdAt", "2023-03-01"
						)
				),
				new Document(
						"Article: Binary Search in Java - How it works?. Searching and sorting are fundamental concepts in programming. Learn how to implement a binary search algorithm that traverses a list or array in Java. Type: Article. Published on Medium in 2023-02. Topics: Computer Science, Search, Java. Featured on the website: no. Link: https://medium.com/javarevisited/binary-search-in-java-how-it-works-da479e74da5.",
						Map.of(
								"title", "Binary Search in Java - How it works?",
								"category", "article",
								"createdAt", "2023-02-01"
						)
				),
				new Document(
						"Article: What is a Binary Tree?. Trees are one of the most fundamental data structures for storing data. A binary tree is defined as a data structure organized in a binary way, where each node has at most two children. Type: Article. Published on Medium in 2023-02. Topics: Algorithms, Data Structure, Java. Featured on the website: no. Link: https://medium.com/javarevisited/what-is-a-binary-tree-1b389b05fec2.",
						Map.of(
								"title", "What is a Binary Tree?",
								"category", "article",
								"createdAt", "2023-02-01"
						)
				),
				new Document(
						"Video: Build a Java RAG Application in 3 Steps.. Learn how to build a RAG application using Java, MongoDB Atlas, LangChain4j, and OpenAI. Published in 2026-07. Duration: 01:33 min. Watch at: https://www.youtube.com/shorts/f7qtqqLp-jE.",
						Map.of(
								"title", "Build a Java RAG Application in 3 Steps.",
								"category", "video",
								"createdAt", "2026-07-01"
						)
				),
				new Document(
						"Video: A complete football betting platform built with Java, Angular, and MongoDB.. A real-world application that automates football betting pools, including match predictions, scoring rules, live rankings, payments, and prize distribution. Built with Java, Spring Boot, Angular, MongoDB Atlas, Keycloak, Docker, and deployed to production Published in 2026-06. Duration: 11:37 min. Watch at: https://www.youtube.com/watch?v=oBvNd25MG10.",
						Map.of(
								"title", "A complete football betting platform built with Java, Angular, and MongoDB.",
								"category", "video",
								"createdAt", "2026-06-01"
						)
				),
				new Document(
						"Video: Tutorial: Building a Kotlin and Quarkus Application | MongoDB Aggregation Framework and Queries. This video demonstrates how to set up a simple application using Quarkus Panache connected to a MongoDB database, followed by executing an aggregation pipeline on a movie dataset to evaluate how the stack performs under data-intensive workloads. Published in 2026-05. Duration: 13:43 min. Watch at: https://www.youtube.com/watch?v=tWZOwQT1C_Q.",
						Map.of(
								"title", "Tutorial: Building a Kotlin and Quarkus Application | MongoDB Aggregation Framework and Queries",
								"category", "video",
								"createdAt", "2026-05-01"
						)
				),
				new Document(
						"Video: Kotlin + Quarkus: Working with MongoDB Aggregation Framework. A short video evaluating the compatibility and performance of Kotlin, Quarkus, and MongoDB in a real-world development scenario. Published in 2026-04. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/-gQ9LXyEACQ.",
						Map.of(
								"title", "Kotlin + Quarkus: Working with MongoDB Aggregation Framework",
								"category", "video",
								"createdAt", "2026-04-01"
						)
				),
				new Document(
						"Video: MongoDB Atlas Full-Text Search Tutorial. Quick intro to full-text search in MongoDB Atlas — how to set up and query a search index. Published in 2026-03. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/3VOcbdVJ63s.",
						Map.of(
								"title", "MongoDB Atlas Full-Text Search Tutorial",
								"category", "video",
								"createdAt", "2026-03-01"
						)
				),
				new Document(
						"Video: How to Create Indexes in Spring Data MongoDB | Single and Compound Indexes Explained. Short showing how to create single-field and compound indexes in a Spring Data MongoDB application. Published in 2026-02. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/25RHDbGGjOE.",
						Map.of(
								"title", "How to Create Indexes in Spring Data MongoDB | Single and Compound Indexes Explained",
								"category", "video",
								"createdAt", "2026-02-01"
						)
				),
				new Document(
						"Video: MongoDB Aggregation Framework Explained in 2 Minutes | A Beginner's Guide. A quick beginner-friendly overview of the MongoDB Aggregation Framework — how to filter, group, and transform data in your pipelines. Published in 2025-11. Duration: 2 min. Watch at: https://www.youtube.com/watch?v=CbYitR4mR6I.",
						Map.of(
								"title", "MongoDB Aggregation Framework Explained in 2 Minutes | A Beginner's Guide",
								"category", "video",
								"createdAt", "2025-11-01"
						)
				),
				new Document(
						"Video: How to Migrate from Relational Databases to MongoDB | Relational Migrator 101. Step-by-step walkthrough of MongoDB's Relational Migrator tool to move your schema and data from a relational database to MongoDB Atlas. Published in 2025-03. Duration: —. Watch at: https://www.youtube.com/watch?v=Z6D5Ge4M2KU.",
						Map.of(
								"title", "How to Migrate from Relational Databases to MongoDB | Relational Migrator 101",
								"category", "video",
								"createdAt", "2025-03-01"
						)
				),
				new Document(
						"Video: Should YOU Migrate from Relational Databases to Build Modern Applications?. Exploring why developers are moving away from relational databases and what MongoDB offers for building modern, flexible applications. Published in 2025-02. Duration: —. Watch at: https://www.youtube.com/watch?v=2Rguel3HG78.",
						Map.of(
								"title", "Should YOU Migrate from Relational Databases to Build Modern Applications?",
								"category", "video",
								"createdAt", "2025-02-01"
						)
				),
				new Document(
						"Video: Indexes: Strategies to Improve Performance. Webinar covering what MongoDB indexes are, how they work, and strategies to optimize query performance and boost database efficiency. Published in 2025-01. Duration: ~60 min. Watch at: https://www.mongodb.com/resources/products/platform/webinar-indexes-strategies-to-improve-performance.",
						Map.of(
								"title", "Indexes: Strategies to Improve Performance",
								"category", "video",
								"createdAt", "2025-01-01"
						)
				),
				new Document(
						"Video: Optimize Spring Data with MongoDB. Quick tips on how to optimize your Spring Data MongoDB application for better performance. Published in 2024-12. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/H-pMOfSrOI8.",
						Map.of(
								"title", "Optimize Spring Data with MongoDB",
								"category", "video",
								"createdAt", "2024-12-01"
						)
				),
				new Document(
						"Video: Build Powerful Search with Kotlin & MongoDB. Short walkthrough on implementing powerful search functionality using Kotlin and MongoDB. Published in 2024-11. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/dHBaiYIpshk.",
						Map.of(
								"title", "Build Powerful Search with Kotlin & MongoDB",
								"category", "video",
								"createdAt", "2024-11-01"
						)
				),
				new Document(
						"Video: Spring Data Unlocked: A Comprehensive Java and MongoDB Series. Overview of the Spring Data Unlocked series — covering Java, Spring Data, and MongoDB from getting started to advanced queries. Published in 2024-11. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/_AwCc47ImtI.",
						Map.of(
								"title", "Spring Data Unlocked: A Comprehensive Java and MongoDB Series",
								"category", "video",
								"createdAt", "2024-11-01"
						)
				),
				new Document(
						"Video: Mastering Spring Data MongoDB for Better Developer Productivity. In this livestream, we’ll cover how to use Spring Data and MongoDB to improve developer efficiency. Mark will explain the basics of Spring Data and repositories, while Christoph will dive into MongoDB features like update methods, geospatial support, and transactions. We'll also discuss extending Spring Data with vector search and creating custom extensions. Published in 2024-10. Duration: 55.31 min. Watch at: https://www.youtube.com/watch?v=w4lvxI47QnU.",
						Map.of(
								"title", "Mastering Spring Data MongoDB for Better Developer Productivity",
								"category", "video",
								"createdAt", "2024-10-01"
						)
				),
				new Document(
						"Video: Engage with the MongoDB Developer Community! | How to Post Comments in Forums. How to participate in the MongoDB Developer Community forums and post comments. Published in 2024-08. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/-sgCimDSgxo.",
						Map.of(
								"title", "Engage with the MongoDB Developer Community! | How to Post Comments in Forums",
								"category", "video",
								"createdAt", "2024-08-01"
						)
				),
				new Document(
						"Video: Building a Kotlin App with Spring Boot and Atlas Search: A Complete Guide. Complete guide to building a Kotlin application using Spring Boot integrated with MongoDB Atlas Search. Published in 2024-05. Duration: —. Watch at: https://www.youtube.com/watch?v=b0dkQYcvBkQ.",
						Map.of(
								"title", "Building a Kotlin App with Spring Boot and Atlas Search: A Complete Guide",
								"category", "video",
								"createdAt", "2024-05-01"
						)
				),
				new Document(
						"Video: Mastering Kotlin: Creating an API With Ktor and MongoDB Atlas. Short intro to building an API with Kotlin, the Ktor framework, and MongoDB Atlas. Published in 2024-02. Duration: < 1 min. Watch at: https://www.youtube.com/shorts/OBmz-1Nbv50.",
						Map.of(
								"title", "Mastering Kotlin: Creating an API With Ktor and MongoDB Atlas",
								"category", "video",
								"createdAt", "2024-02-01"
						)
				),
				new Document(
						"Video: MongoDB Compass - Convert Text into Queries with AI-Powered Natural Language. In this new tutorial, we will explore the latest feature from MongoDB Compass - Query With Natural Language. Compass employs artificial intelligence to create filter queries and aggregations based on the prompts you supply. Published in 2024-02. Duration: 7:58 min. Watch at: https://www.youtube.com/watch?v=3Qtzxrp5iO0.",
						Map.of(
								"title", "MongoDB Compass - Convert Text into Queries with AI-Powered Natural Language",
								"category", "video",
								"createdAt", "2024-02-01"
						)
				),
				new Document(
						"Video: Understanding MongoDB Replication: A Step-by-Step Replica Set Creation. In this video, we will delve into the importance of Replication, uncovering the problems it solves. We'll explore the concept of scalable reads and discover how to boost performance through read preference. Published in 2024-01. Duration: 18:12 min. Watch at: https://www.youtube.com/watch?v=ZGHowQHMOoM.",
						Map.of(
								"title", "Understanding MongoDB Replication: A Step-by-Step Replica Set Creation",
								"category", "video",
								"createdAt", "2024-01-01"
						)
				),
				new Document(
						"Video: Relational Migrator - Continuous Sync Jobs PostgreSQL to MongoDB Atlas. This brief video demonstrates Relational Migrator's continuous job integration, showcasing a straightforward Change Data Capture (CDC) process from PostgreSQL to MongoDB Atlas. Published in 2023-12. Duration: 0:56 min. Watch at: https://www.youtube.com/watch?v=nvKm4PCEmC0.",
						Map.of(
								"title", "Relational Migrator - Continuous Sync Jobs PostgreSQL to MongoDB Atlas",
								"category", "video",
								"createdAt", "2023-12-01"
						)
				),
				new Document(
						"Project: MongoDB CLI Lab. An interactive, browser-based CLI environment for learning MongoDB from scratch — no install required. A Node.js CLI to spin up local MongoDB environments with Docker — designed for learning, demos, and development. Standalone, replica set, sharded cluster, MongoDB Search, and Queryable Encryption. Technologies/topics: MongoDB, Interactive, Education, CLI, Next.js. Featured on the website: no. Project link: https://mongodb-cli-lab.vercel.app/.",
						Map.of(
								"title", "MongoDB CLI Lab",
								"category", "project",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Project: G12 Football Bet. A complete football betting platform built with Java, Angular, and MongoDB. A real-world application that automates football betting pools, including match predictions, scoring rules, live rankings, payments, and prize distribution. Built with Java, Spring Boot, Angular, MongoDB Atlas, Keycloak, Docker, and deployed to production. Technologies/topics: Java, Spring Boot, Angular, MongoDB, Keycloak, Docker. Featured on the website: yes. Project link: https://youtu.be/gn2cshtgF5U?si=Y-paTNNDMrEstxMU.",
						Map.of(
								"title", "G12 Football Bet",
								"category", "project",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"""
						Project: RicAI — Personal Content Agent.

						RicAI is an AI agent developed to help users discover
						information about Ricardo Mello's professional background, articles,
						videos, talks, events, and projects.

						The application was developed with Java 21, Spring Boot, and Spring AI.
						It uses OpenAI models for natural-language understanding, response
						generation, embeddings, planning, and tool selection.

						MongoDB Atlas is used as the main database. It stores the content
						knowledge base, vector embeddings, conversation memory, semantic cache,
						and persisted execution plans.

						The application combines Retrieval-Augmented Generation (RAG) with
						structured tools. Semantic search uses MongoDB Atlas Vector Search for
						questions based on meaning or topic. Structured MongoDB queries are used
						when deterministic results are required, including counting, filtering,
						chronological ordering, date ranges, and upcoming events.

						The assistant supports conversation memory, allowing it to preserve
						context between messages using a conversation identifier. It also uses
						semantic caching to reuse answers to sufficiently similar questions when
						the operation is eligible for caching.

						For compound requests, the application has an agentic planning layer.
						The AI determines whether a request requires multiple dependent steps,
						creates a short execution plan, and persists that plan in MongoDB.

						Each plan contains an objective, ordered tasks, execution status,
						intermediate results, errors, timestamps, and the names of the tools
						used by each task. The execution layer runs one task at a time, provides
						previous results as context for subsequent tasks, persists progress, and
						synthesizes a final response after completing the plan.

						The application also integrates with Calendly. It can retrieve available
						meeting times and generate a scheduling link after collecting the
						required information and obtaining the appropriate user confirmation.

						The project is packaged as a Docker container using a Dockerfile and is
						hosted on Google Cloud Run. Its source code is available on GitHub:
						https://github.com/ricardohsmello/personal-content-rag.

						Technologies and concepts: Java 21, Spring Boot, Spring AI, OpenAI,
						MongoDB Atlas, MongoDB Vector Search, RAG, AI agents, tool calling,
						dynamic planning, multi-step execution, conversation memory, semantic
						cache, Docker, Google Cloud Run, and Calendly.
						""",
						Map.of(
								"title", "RicAI — Personal Content Agent",
								"category", "project",
								"createdAt", "2026-08-12"
						)
				),
				new Document(
						"Certifications / certificações held by Ricardo Mello: Java SE Programmer, issued by Oracle Certified in 2016. Credential: https://www.credly.com/badges/fd63620f-f45a-415d-b564-03b36a2d7c80/linked_in_profile | Associate Developer, issued by MongoDB Certified in 2024. Credential: https://www.credly.com/badges/db3d6169-9202-4c38-a4dc-d13036ca95c0/linked_in_profile.",
						Map.of(
								"title", "Ricardo Mello - Certifications",
								"category", "certification",
								"createdAt", "2026-08-10"
						)
				),
				new Document(
						"Contact and social networks / contato e redes sociais of Ricardo Mello: GitHub: https://github.com/ricardohsmello | LinkedIn: https://linkedin.com/in/ricardohsmello | Twitter / X: https://twitter.com/ricardohsmello | Medium: https://ricardohsmello.medium.com | Foojay.io: https://foojay.io/today/author/ricardo-mello/ | YouTube: https://youtube.com/channel/UC1OioM3DPJL599iYkejNg0Q | Dev.to: https://dev.to/ricardohsmello | Calendly: https://calendly.com/ricardohsmello/30min. Ricardo is open to speaking engagements, workshops, technical writing, content collaboration, technical questions, and conversations about technology.",
						Map.of(
								"title", "Ricardo Mello - Contact and Social Networks",
								"category", "contact",
								"createdAt", "2026-08-10"
						)
				)
		);
		vectorStore.add(listDocuments);
	}
}
