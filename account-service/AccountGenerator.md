# 🏦 Enterprise Account Number Generator

## Overview

This module is responsible for generating:

- unique bank account numbers
- branch-specific account numbers
- checksum-protected account numbers
- concurrency-safe account numbers

It is designed similar to real banking systems where account numbers must:

- never duplicate
- survive application restart
- support multiple application instances
- scale under heavy traffic
- detect typing mistakes

---

## 🎯 Goal of This System

The generator creates account numbers in this format:

`[BBB][TT][SSSSS][CC]`

**Example:**

`101100000135`

**Meaning:**

| Part | Meaning |
|------|---------|
| 101 | Branch code |
| 10 | Account type code |
| 00001 | Sequence |
| 35 | Checksum |

---

## 🏦 Real-World Banking Analogy

Imagine a large bank:

- many branches
- many employees
- many counters
- thousands of account openings daily

The bank needs:

- unique account numbers
- no duplicates
- safe generation under heavy traffic

This generator acts like:

**Central Account Number Allocation Department**

---

## 📦 AccountType Enum

```java
public enum AccountType {
    SAVINGS("10"),
    CURRENT("11"),
    FIXED_DEPOSIT("13"),
    RECURRING_DEPOSIT("14"),
    LOAN("20");
}
```

### Purpose

Maps banking products to account type codes.

Instead of:

``` java if(type.equals("SAVINGS")) ```

we use:

``` java accountType.getCode() ```

### Why This Is Better

| Old Approach | Better Approach |
|---|---|
| if-else chains | enum mapping |
| hardcoded logic | centralized configuration |
| typo-prone | type-safe |
| hard to scale | maintainable |

---

## 🗄️ AccountNumberSequence Entity

```java
@Entity
public class AccountNumberSequence {
    private String branchCode;
    private AccountType accountType;
    private Long nextValue;
    private Long maxValue;
    private Boolean active;

    @Version
    private Long version;
}
```

---

## 🧠 Purpose of Each Field

| Field | Purpose |
|-------|---------|
| branchCode | branch isolation |
| accountType | product separation |
| nextValue | next usable sequence |
| maxValue | allocated block limit |
| active | enable/disable sequence |
| version | optimistic locking |

---

## 🔒 Why Optimistic Locking?

```java
@Version
private Long version;
```

Prevents duplicate account numbers during concurrent requests.

### Example Problem

Two requests:

- Request A -> reads sequence 100
- Request B -> reads sequence 100

Both try generating:

- 101

**Without locking:** ❌ duplicate account numbers.

**With optimistic locking:** ✅ only one update succeeds.

---

## 🧱 Block Allocation Strategy

```java
private static final long BLOCK_SIZE = 1000;
```

### Why?

Instead of hitting DB for every account generation:

```
1 request = 1 DB update
```

we allocate ranges.

### Example:

| Server | Allocated Range |
|--------|-----------------|
| Server A | 1 → 1000 |
| Server B | 1001 → 2000 |

This improves:

- performance
- scalability
- concurrency

---

## ⚙️ Generator Service Flow

```
Request arrives
    ↓
Validate branch
    ↓
Fetch sequence row
    ↓
Allocate next sequence
    ↓
Increment counter
    ↓
Generate checksum
    ↓
Return account number
```

---

## 🏦 Main Generate Method

```java
@Transactional
public String generate(
    Branch branch,
    AccountType accountType
)
```


---

## 🔄 Transaction Management

```java
@Transactional
```

Ensures:

- sequence update
- account generation

happen atomically.

### Why Important?

Suppose:

- sequence updated
- BUT app crashes before account save

**Without transaction:** ❌ inconsistent data.

**With transaction:** ✅ rollback protection.

---

## 🔁 Retry Mechanism

```java
for (int retry = 0; retry < 3; retry++)
```

If optimistic locking fails:

- retry automatically
- avoid immediate failure

### Real-World Analogy

Two bank clerks try issuing same token.

One succeeds. Second retries.

---

## 🧾 Sequence Retrieval

```java
sequenceRepository
    .findByBranchCodeAndAccountType(...)
```

Finds latest sequence for:

- branch
- product

### Example

Delhi branch + Savings account

may have:

- latest sequence = 4521

---

## 🆕 Sequence Initialization

```java
.orElseGet(() -> {
```

If sequence does not exist:

- create new row
- start from 1

Used when:

- first account ever created
- for that branch/product.

---

## 📈 Sequence Increment

```java
sequence.setNextValue(currentSequence + 1);
```

Reserves next account number.

### Example:

| Previous | Next |
|----------|------|
| 1 | 2 |
| 999 | 1000 |

---

## 🚫 Sequence Exhaustion Protection

```java
if (currentSequence > 99999)
```

Because sequence part supports only:

- 00001 → 99999

Prevents overflow.

---

## 🧮 Sequence Formatting

```java
String.format("%05d", currentSequence)
```

Formats:

| Input | Output |
|-------|--------|
| 1 | 00001 |
| 25 | 00025 |
| 451 | 00451 |

Maintains fixed-length banking format.

---

## 🏦 Building Base Account Number

```java
String base =
    branchCode +
    accountType.getCode() +
    formattedSequence;
```

### Example:

```
101 + 10 + 00001
```

**Result:**

```
1011000001
```


---

## 🔐 Luhn Checksum

```java
generateLuhnChecksum(base)
```

Adds checksum digits.

### Purpose

Detects:

- typing mistakes
- accidental swaps
- invalid account numbers

Used in:

- debit cards
- credit cards
- IMEI numbers

---

## 🏦 Final Account Number

```
101100000135
```

**Breakdown:**

| Part | Value |
|------|-------|
| 101 | branch |
| 10 | savings |
| 00001 | sequence |
| 35 | checksum |

---

## 🚀 Enterprise Features Supported

| Feature | Supported |
|---------|-----------|
| Persistent sequence | ✅ |
| Restart safe | ✅ |
| Optimistic locking | ✅ |
| Retry mechanism | ✅ |
| Branch isolation | ✅ |
| Sequence exhaustion protection | ✅ |
| Transaction safety | ✅ |
| Checksum validation | ✅ |
| Distributed-safe design | ✅ |
| Scalable architecture | ✅ |

---

## 🏦 Why This Is Better Than Random Numbers

| Random Generator | Enterprise Generator |
|---|---|
| collision risk | safe sequencing |
| not readable | structured format |
| no branch info | branch-aware |
| no checksum | checksum validation |
| restart issues | persistent |
| JVM-only | distributed-safe |

---

## 🔮 Future Enterprise Enhancements

Possible future upgrades:

- Redis distributed locking
- Dedicated ID generation microservice
- Kafka event publishing
- Snowflake IDs
- Outbox pattern
- Multi-region replication
- Event sourcing
- Saga orchestration

---

🧠 Key Backend Engineering Concepts Used

Concept	Why Important

optimistic locking	concurrency safety
transactions	consistency
checksum	validation
block allocation	scalability
retries	resilience
enum mapping	maintainability
sequence isolation	distributed scaling

---

🏁 Final Summary

This account number generator is designed using enterprise backend engineering principles.

It solves:

 - duplicate prevention
 - concurrent access
 - scalability
 - structured business identifiers
 - checksum validation
 - restart safety


instead of treating account generation as:
just random number generation
This architecture is much closer to how real banking systems think about:

 - identifiers
 - concurrency
 - distributed systems
 - auditability
 - scalability.