# NLP [![Build Status](https://travis-ci.org/cglirang/NLP.svg?branch=master)](https://travis-ci.org/cglirang/NLP)
Natural Language Processing test project

---
## 1. Components
### 1.1 Keywords Extraction
> Automatic identification of terms that best describe the subject of a document.

#### 1.1.1 RAKE: Rapid Automatic Keyword Extraction
+ A java implementation of text mining algorithm described in the paper 
[Automatic Keyword Extraction from Individual Documents](https://www.researchgate.net/publication/227988510_Automatic_Keyword_Extraction_from_Individual_Documents)
by Rose et.al.
+ Based on [JRAKE](https://github.com/AskDrCatcher/JRAKE). 

---
### 1.2 Full-text Search
> In text retrieval, full-text search refers to techniques for searching a single computer-stored document or a collection in a full text database. Full-text search is distinguished from searches based on metadata or on parts of the original texts represented in databases (such as titles, abstracts, selected sections, or bibliographical references).

#### 1.2.1 Lucene Searcher
+ Document ranking in keywords search by Apache Lucene. 

---
### 1.3 Data Pretreatment
> The term data pretreatment refers to a range of preliminary data characterization and processing steps that precede detailed analysis using standard methods. 

#### 1.3.1 Dependency Parser
+ A dependency parser analyzes the grammatical structure of a sentence, establishing relationships between "head" words and words which modify those heads.
+ Generate dataset for text classification. 

#### 1.3.2 Friends Scenario Cutter

---
### 1.4 Sentiment analysis
> Sentiment analysis (sometimes known as opinion mining or emotion AI) refers to the use of natural language processing, text analysis, computational linguistics, and biometrics to systematically identify, extract, quantify, and study affective states and subjective information.

#### 1.4.1 Complain Detection
+ Analysis the input sentence, determine whether the user is complaining. 
+ Integrate the results of Stanford CoreNLP sentiment analysis and curse dic matching 

---
## 2. Required Jar Packages
+ [Apache Lucene](http://lucene.apache.org/) Version 6.6.0
+ [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/) Version 3.8.0
