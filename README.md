# Algorithm Performance Evaluator

A desktop application designed to bridge the gap between theoretical Big-O analysis and real execution behavior.
The system allows users to write custom algorithms, run them on different input sizes, and automatically estimate their time complexity.

---

## Features

* Write and test custom algorithms
* Execute code safely in an isolated environment
* Measure execution time across multiple input sizes
* Automatic complexity estimation (Big-O)
* Multiple input modes:
  * Random arrays
  * Sorted arrays
  * Reversed arrays
* Visual representation of performance

---

## Architecture

The system is divided into two main parts:

### Front-End (JavaFX)

* User interface for writing code and viewing results
* Handles user input and visualization

### Back-End (FastAPI)

* Input generation
* Code execution
* Runtime measurement
* Complexity analysis

---

## Technologies Used

* **Front-End:** Java, JavaFX
* **Back-End:** Python, FastAPI
* **Networking:** HTTP (API Communication)
* **JSON Processing:** Gson
* **Visualization:** JavaFX Charts

---

## ▶ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/Youssef-Reda440/Algorithm-Performance-Evaluator
cd Algorithm-Performance-Evaluator
```

### 2. Run the Back-End

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```

### Run the Front-End

```bash
cd frontend
mvn clean javafx:run
```

---

## How It Works

1. User writes an algorithm in the UI
2. The code is sent to the backend via API
3. The system:
   * Generates inputs
   * Executes the code
   * Measures runtime
4. The Complexity Analyzer compares results with known complexity functions
5. The best match is returned with a confidence score

---

## UI

![Dashboard]{Assets/Dashboard.png}

---

## References

* Python Docs: https://docs.python.org/3/
* FastAPI Docs: https://fastapi.tiangolo.com/
* Java Docs: https://docs.oracle.com/en/java/
* JavaFX Docs: https://openjfx.io/
* Gson: https://github.com/google/gson
