# 📦 GCP Bucket File Importer (Spring Batch)

This project is a **Spring Batch-based data import solution** designed to read Excel files from a **Google Cloud Storage (GCS) bucket**, process the data, and store it into a **PostgreSQL** database. It is optimized for large-scale batch operations, with built-in support for chunk processing, concurrency, fault tolerance, and job restartability.

## Architecture
The following picture illustrates the architecture of this batch process
![importFile (1)](https://github.com/user-attachments/assets/c866b313-cc9a-4933-b560-73c09906b83c)

### 🔄 How It Works

- **Data Source**: The batch job reads Excel files stored in a **Google Cloud Storage (GCS) bucket**.
- **Data Processing**: Each record from the Excel file is optionally transformed or validated during processing.
- **Data Target**: Processed data is stored in a **PostgreSQL** database.
- **Chunk-Oriented Processing**: Data is handled in chunks to optimize memory usage and improve performance.
- **Parallel Execution**: Steps can run concurrently to handle large datasets faster.
- **Transactional Control**: Each chunk is processed within a transaction, ensuring consistency and enabling safe retries.

### 💡 Key Components

- **Reader**: Loads data from Excel files in GCS.
- **Processor**: (Optional) Applies transformations or validation to each item.
- **Writer**: Saves processed records to the PostgreSQL database.

### ✅ Features

- **Cloud Integration**: Reads data directly from Google Cloud Storage.
- **Restartable Jobs**: Resumes from failure points without reprocessing everything.
- **Fault Tolerance**: Skips or retries problematic records as configured.
- **Monitoring Support**: Built-in job tracking and logging.
- **Flexible Configuration**: Easily adapts to different Excel formats, data schemas, and processing rules.
