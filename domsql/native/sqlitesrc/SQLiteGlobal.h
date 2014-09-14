/* This file include the Domino SQLLite global options*/

// When this C-preprocessor macro is defined, SQLite includes some additional APIs that 
// provide convenient access to meta-data about tables and queries
#define SQLITE_ENABLE_COLUMN_METADATA

// Make multithreaded safe, (Serialized)
#define SQLITE_THREADSAFE 1


//#define SQLITE_DEBUG 1