# MARKET-BASKET ANALYSIS
Tale progetto di data mining si focalizza sul reperire informazioni riguardanti pattern frequenti ed
emergenti dati due dataset.
Per poter estrapolare questi informazioni, l'utente ha il compito di inserire, attraverso un interfaccia d'input,
i valori support e grow rate minimi e successivamente di selezionare i due set di dati sui quali effettuare l'analisi:
dataset Target e dataset Background; presenti nel database.
- La versione base di questo progetto è organizzata secondo un modello client-server. Qui il client stabilisce se ricercare i pattern nell'archivio o reperirli subito attraverso una ricerca nei due dataset. Subito dopo inserisce i valori di grow rate e support, e di dataset Target e dataset Background. Una volta ricevute queste informazioni, il server si occuperà di calcolarle e di inviare i risultati dei pattern frequenti ed emergenti al client, che dovrà poi visualizzarli. Inoltre, questa versione del progetto sfrutta il multithreading favorendo nello stesso momento la connessione di più client.
- La versione finale, invece, estende delle funzionalità ovvero una interfaccia grafica per dispositivi Android migliorando l'esperienza utente.
### La differenza sostanziale tra il progetto base e l'estensione è che l'applicazione Android opera solo in locale e perciò incorpora al suo interno il database e tutti i rispettivi calcoli per generare i due pattern.