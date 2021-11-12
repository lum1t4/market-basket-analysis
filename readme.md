# MARKET-BASKET ANALYSIS
Tale progetto di data mining si focalizza sul reperire informazioni riguardanti pattern frequenti ed
emergenti dati due dataset.
Per poter estrapolare questi informazioni, l'utente ha il compito di inserire, attraverso un interfaccia d'input,
i valori support e grow rate minimi, e successivamente di selezionare i due set di dati sui quali effettuare l'analisi,
dataset Target e dataset Background, presenti nel database.
- La versione base di questo progetto è organizzata secondo un modello client-server. Qui il client stabilisce se ricercare i pattern nell'archivio o reperirli subito attraverso una ricerca nei due dataset. Subito dopo inserisce i valori di grow rate e support, e di dataset Target e dataset Background. Una volta ricevute queste informazioni, il server si occuperà di calcolarle e di inviare i risultati dei pattern frequenti ed emergenti al client, che dovrà poi visualizzarli. Inoltre, questa versione del progetto estende il concetto di multithreading favorendo nello stesso momento la connessione di più client verso lo stesso server.
- La versione finale, invece, estende delle funzionalità che ha permesso di realizzare un'applicazione per dispositivi Android. 
### La differenza sostanziale tra il progetto base e l'estensione è che l'applicazione Android opera in locale senza la presenza di Client e Server dato che incorpora al suo interno il database e tutti i rispettivi calcoli per generare i due pattern. L'applicazione, cioè l'estensione, presenta in più un interfaccia grafica che migliora l'esperienza dell'utente. 
