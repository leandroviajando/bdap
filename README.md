# BDAP

## Setup

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements-dev.txt
pip install -r requirements.txt
```

### Running Experiments with `screen` (or `tmux`)

Useful for running experiments remotely, without maintaining an active shell session: multiple shell windows from single SSH session, stay active without network connection, can disconnect and reconnect.

Start:

```bash
screen
>>ENTER
```

Close:

- terminate (kills processes): `<CTRL>-D` / `exit`
- detach (remain in background) `<CTRL>-A+D`
  - re-attach: `screen -r`

Other commands:

- detach screen: `<CTRL>-A+D`
- create new terminal within screen: `<CTRL>-A+C`
- go to next terminal: `<CTRL>-A+N`
- go to previous terminal: `<CTRL>-A+P`
- help page: `<CTRL>-A+?`

## How to Cope with Big Data?

### Novel paradigms for storing data

#### Move Away From Traditional Table-Based Storage

##### Analysis: Column store RDBMS or OLAP

###### Data Warehouse

- Subject oriented
- Integrate multiple data sources
- Time variant
- **Schemas for Warehouses**
  - Star schema
  - Snowflake
  - Fact constellations
- **OLAP Architectures**
  - ROLAP (Relational OLAP)
  - MOLAP (Multidimensional OLAP)

##### Key-value storages, e.g., Dynamo

###### Primary key access

###### Consistent Hashing

- Nodes Joining and Leaving

###### Redundancy: Replicate Data

###### Quorom (R, W, N)

###### Gossip for membership

##### Document databases, e.g., MongoDB

###### Nested values, no global schema

##### Add functionality: flexible schema (NoSQL)

###### Document

###### Extensible record

##### Scale up to distributed environments

###### Scaling out / Horizontal scaling

- Primary-secondary
- Sharding

###### CAP Theorem (Consistency, Availability, Partition tolerance)

##### Possibly give up ACID guarantees

#### Distributed File Systems

##### Global file namespace

##### Huge files, rare in-place updates

##### Chunk Servers (split into chunks, replication)

##### Primary / main node (metadata)

#### Data Structures for Approximations

##### Summarize the Data

##### Probabilistic Counting: Flajolet-Martin (distinct elements)

##### Bloom Filter (seen element before, reducing false positives)

##### Misra Gries (frequent items, merging summaries)

##### Count-Min Sketch (approximate counts, matrix with hash functions)

### Better analysis techniques

#### Clever Retrieval Structures

##### Quad Trees (2D spatial data)

##### KD Trees (multi-dimensional data)

##### B+ trees (range queries)

##### Bit indexes (multiple selections, aggregates)

##### Inverted indexes (not explicitly detailed but implied for search)

#### Limit Number of Comparisons

##### Hashing into buckets

###### Hash Join

###### Static Hashing (indexing in main memory)

###### Extendable Hashing (dynamic buckets)

##### Locality Sensitive Hashing (LSH) (nearest neighbor search)

###### Shingling (document to sets)

###### Minhash (signatures preserving similarity)

###### Banding (finding candidate pairs)

###### AND/OR Constructions (amplifying LS-Family)

#### Approximate the Answer

##### "Randomized" data structures

###### Counts (HyperLogLog, CountMin)

##### Sampling

###### Fixed database sampling

###### Stream sampling (bottom-k, weighted)

#### Data Mining & ML Algorithms

##### K Nearest Neighbors (kNN)

##### Online Learning

###### Very Fast Decision Trees (VFDT) (streaming data)

##### Product Quantization (approximate nearest neighbor)

#### Spark SQL

##### Structured data processing

##### SchemaRDD abstraction

##### Aggregate functions (groupby, window)

##### Join function

##### Write function (coalesce, options)

### Massive computational power (New Processing Paradigms)

#### Perform computations near data

#### Handle aspects of distribution

#### Gracefully handle failures

#### Facilitate different types of parallelism and workloads

##### Data parallel

###### MapReduce

- Map, Group by (Shuffle/Sort), Reduce
- Distributed Execution
- Fault Tolerance
- Combiner, Partition Function
- Examples (Word Count, Join, Matrix Multiplication)

###### Spark

- **Resilient Distributed Datasets (RDDs)**
  - Transformations, Actions
  - Persistence, Partitioning
  - Lineage and Recovery
- Advantages for iterative processing
- Examples (Word Count, Log Mining, Logistic Regression)

##### Graph parallel

###### GraphLab

- Graph-based data representation (vertices and edges with data)
- Update functions
- Scheduler, Consistency Model (e.g., Edge Consistency)

#### Parallel Processing Fundamentals

##### Shared Memory vs. Distributed Memory Architectures

##### Message Passing (MPI)

##### Job Management (Condor, DAGMan)
