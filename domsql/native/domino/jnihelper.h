/*
** (c) Copyright IBM Corp. 2014
** 
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at:
** 
** http://www.apache.org/licenses/LICENSE-2.0 
** 
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
** implied. See the License for the specific language governing 
** permissions and limitations under the License.
*/

#ifndef _JNIHELPER_H_
#define _JNIHELPER_H_


// WARN: this should be adapted on some platforms, like zOS
template<class T> inline jlong toHandle(T* ptr) {
	return (jlong)ptr;
}
template<class T> inline T* toPointer(jlong handle) {
	return (T*)handle;
}



// ---------------------------------------------------------------------------
// Class and member accessors
bool JNIFindClass(JNIEnv* env, jclass& c, const char* className);
bool JNIGetStaticMethodID(JNIEnv* env, jmethodID& m, jclass c, const char* className, const char* sig);


// ---------------------------------------------------------------------------
// Memory management routines
inline void* MemAlloc(size_t sz) { return ::malloc(sz); }
inline void MemFree(void* ptr) { if(ptr){::free(ptr);} }

class MemoryManagedObject {
public:
	void *operator new(size_t sz) { return MemAlloc(sz); }
	void operator delete(void *ptr) { MemFree(ptr); }
};

inline size_t min(size_t s1, size_t s2)  { return s1<s2 ? s1 : s2; }
inline size_t max(size_t s1, size_t s2)  { return s1>s2 ? s1 : s2; }


// ---------------------------------------------------------------------------
// String routines
inline const char* allocString(const char* source) {
	if(source) {
		size_t len = strlen(source);
		return (char*)memcpy((void*)MemAlloc(len+1),source,len+1);
	}
	return NULL;
}
inline const char* allocString(const char* source, size_t len) {
	if(source) {		
		char* s=(char*)memcpy((void*)MemAlloc(len+1),source,len);
		s[len] = 0;
		return s;
	}
	return NULL;
}
inline void freeString(const char* source) {
	MemFree((void*)source);
}

jsize jstrlen(const jchar *str);

class MemString {
	const char* str;
public:
	MemString() {
		this->str = NULL;
	}
	MemString(const char* source) {
		this->str = allocString(source);
	}
	MemString(const char* source, bool alloc) {
		this->str = alloc?allocString(source):source;
	}
	MemString(const char* source, int len) {
		char* s = (char*)MemAlloc(len+1);
		memcpy(s,source,len);
		s[len]=0;
		this->str=s;
	}
	~MemString() {
		MemFree((void*)str);
	}
	MemString& operator =(const char* source) {
		MemFree((void*)str);
		this->str = allocString(source);
		return *this;
	}
	void set(const char* source, size_t len) {
		MemFree((void*)str);
		this->str = allocString(source,len);
	}
	operator const char*() {
		return str;
	}
	const char* detach() {
		const char* s = str;
		str = NULL;
		return s;
	}
	size_t length() {
		return str ? strlen(str) : 0;
	}
	bool isEmpty() {
		return str==NULL || str[0]==0;
	}
	char operator[](int idx) {
		return str[idx];
	}
	bool operator ==(const char* s) {
		return strcmp(str,s)==0;
	}
	bool equals(const char* s) {
		return strcmp(str,s)==0;
	}
	bool equalsIgnoreCase(const char* s) {
		return _stricmp(str,s)==0;
	}
};
	
class ByteBuffer {
	size_t	length;
	void*	bytes;
public:
	ByteBuffer() {
		this->length = 0;
		this->bytes = NULL;
	}
	ByteBuffer(size_t length) {
		this->length = length;
		this->bytes = MemAlloc(length);
	}
	~ByteBuffer() {
		MemFree(bytes);
	}
	void init(size_t length) {
		if(length>this->length) {
			MemFree(bytes);
			this->length = length+1024;	// 1K minimm
			bytes = MemAlloc(this->length);
		}
	}
	size_t size() {
		return length;
	}
	void* getBytes() {
		return bytes;
	}
};

class StringBuilder {
private:
	size_t	count;
	size_t	bufferSize;
	char	buffer[256];
	char*	str;

	void ensure(size_t capacity);

public:
	StringBuilder();
	~StringBuilder();

	size_t length() { return count; }

	const char* toString();
	const char* copy();
	void append(char c);
	void append(const char* s);
	void append(const char* s, size_t length);

	BYTE* getBytes();
	jbyteArray toByteArray(JNIEnv* env);
};

class StringBuilder16 {
private:
	jsize	count;
	jsize	bufferSize;
	jchar*	str;
	jchar	buffer[256];

	void ensure(size_t capacity);

public:
	StringBuilder16();
	~StringBuilder16();

	size_t length() { return count; }

	const jchar* toString();
	const jchar* copy();
	void append(jchar c);
	void append(const jchar* s);
	void append(const jchar* s, size_t length);

	void appendChar(char c);
	void appendChar(const char* c);
	void appendChar(const char* c, int length);
};

class StringParser {
private:
	const char* str;
	size_t	length;
	size_t	ptr;

	bool eof();

public:
	StringParser(const char* str);
	
	bool isString();
	bool isObject();

	jint readInt();
	const char* readString();
	jstring readJString();
	bool readSeparator(char sep);
	bool startObject();
	bool endObject();
};


// Internal class for value translation
class StringParameter {
	friend class JNIEnvironment;

	static const int MAXBUFFER = 128;

    const char*	text;
	char buffer[MAXBUFFER];

	void initJString(jstring js);

  public:
    // ctors
    StringParameter( const StringParameter& from );
	StringParameter( const char* text );
	StringParameter( const char* text, int length );
	StringParameter( MemString& str );
    StringParameter( jstring text );
    StringParameter( const jchar* text );
    StringParameter( const jchar* text, int length );
    StringParameter( char ch );
    StringParameter( unsigned char value );
    StringParameter( short value );
    StringParameter( unsigned short value );
    StringParameter( int value );
    StringParameter( unsigned int value );
    StringParameter( long value );
    StringParameter( unsigned long value );
    StringParameter( __int64 value );
    StringParameter( double value );
    StringParameter( bool value );
    StringParameter( void* value );

    operator const char*() const { return text; }
};

const char* FormatStr( char* dest, int destSize, 
						const char* fmt, 
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );
jstring FormatJString(  JNIEnv* env, const char* fmt, 
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );

// Some utilities
void jprintln(JNIEnv* env, const char* fmt, 
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );
void jprintln(const char* fmt, 
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );

class TraceFunction {
	const char* fctName;
public:
	TraceFunction(const char* fctName,
						const char* fmt=(const char*)0,
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );
	~TraceFunction();
};

class JStringUTF8 {
	JNIEnv* env;
	jstring s;
	jboolean iscopy;
	const char* str;
public:
	JStringUTF8(JNIEnv* env, jstring s) : env(env), s(s) {
		this->str = env->GetStringUTFChars(s, &iscopy);
	}
	~JStringUTF8() {
		if(iscopy) {
			env->ReleaseStringUTFChars(s, str);
		}
	}
	const char* getChars() { return str; }
	bool equals(const char* s) {
		return strcmp(str,s)==0;
	}
};

class JStringUTF16 {
	JNIEnv* env;
	jstring s;
	jboolean iscopy;
	const jchar* str;
public:
	JStringUTF16(JNIEnv* env, jstring s) : env(env), s(s) {
		this->str = env->GetStringChars(s, &iscopy);
	}
	~JStringUTF16() {
		if(iscopy) {
			env->ReleaseStringChars(s, str);
		}
	}
	const jchar* getChars() { return str; }
};

class JStringUTF16C {
	JNIEnv* env;
	jstring s;
	jboolean iscopy;
	const jchar* str;
public:
	JStringUTF16C(JNIEnv* env, jstring s) : env(env), s(s) {
		this->str = env->GetStringCritical(s, &iscopy);
	}
	~JStringUTF16C() {
		if(iscopy) {
			env->ReleaseStringCritical(s, str);
		}
	}
	const jchar* getChars() { return str; }
};


// ---------------------------------------------------------------------------
// Accessing the callback in JNIUtils

#define JNI_VERSION		JNI_VERSION_1_2

class JNIUtils {
public:
	static JavaVM* globalVM;

	static jclass		jniClass;

	static jmethodID	m_createException;
	static jmethodID	m_flush;
	static jmethodID	m_print;
	static jmethodID	m_println;
	static jmethodID	m_addObjectToList;
	static jmethodID	m_addIntToList;
	static jmethodID	m_addLongToList;

	// Debug flags
	static jboolean		FLAG_ENABLE_TRACE;
	static jboolean		FLAG_TRACE_FUNCTION;

	static bool init(JavaVM* globalVM, JNIEnv *env);

	static JNIEnv* getJNIEnv();

	static void throwex(JNIEnv *env);
	static void throwex(JNIEnv *env, const char *str,
						StringParameter p1=(const char*)0,
						StringParameter p2=(const char*)0,
						StringParameter p3=(const char*)0,
						StringParameter p4=(const char*)0,
						StringParameter p5=(const char*)0,
						StringParameter p6=(const char*)0,
						StringParameter p7=(const char*)0,
						StringParameter p8=(const char*)0,
						StringParameter p9=(const char*)0,
						StringParameter p10=(const char*)0 );
	static void throwex(JNIEnv *env, jstring js);

	static jobject createException(JNIEnv* env, jstring msg) {
		return env->CallStaticObjectMethod(jniClass,m_createException,msg);
	}
	static void flush(JNIEnv* env) {
		env->CallStaticVoidMethod(jniClass,m_flush);
	}
	static void print(JNIEnv* env, jstring s) {
		env->CallStaticVoidMethod(jniClass,m_print,s);
	}
	static void println(JNIEnv* env, jstring s) {
		env->CallStaticVoidMethod(jniClass,m_println,s);
	}
	static void addObjectToList(JNIEnv* env, jobject list, jobject value) {
		env->CallStaticVoidMethod(jniClass,m_addObjectToList,list,value);
	}
	static void addIntToList(JNIEnv* env, jobject list, jint value) {
		env->CallStaticVoidMethod(jniClass,m_addIntToList,list,value);
	}
	static void addLongToList(JNIEnv* env, jobject list, jlong value) {
		env->CallStaticVoidMethod(jniClass,m_addLongToList,list,value);
	}
};



// ---------------------------------------------------------------------------
// Management of global JNI references

template <class T> class JNIGlobalRef {
	T		object;
public:
	JNIGlobalRef() {
		this->object = NULL;
	}
	JNIGlobalRef(const T& object) {
		JNIEnv* env = JNIUtils::getJNIEnv();
		this->object = object ? (T)env->NewGlobalRef(object) : NULL;
	}
	JNIGlobalRef(JNIEnv* env, const T& object) {
		this->object = object ? (T)env->NewGlobalRef(object) : NULL;
	}
	~JNIGlobalRef() {
		if(this->object) {
			JNIUtils::getJNIEnv()->DeleteGlobalRef(this->object);
		}
	}
	JNIGlobalRef<T> operator =(const T& object) {
		JNIEnv* env = JNIUtils::getJNIEnv();
		if(this->object) {
			env->DeleteGlobalRef(this->object);
		}
		this->object = object ? (T)env->NewGlobalRef(object) : NULL;
	}
	operator T() const {
		return object;
	}
};


template <class T> class Reference {
	T*	object;
public:
	Reference() {
		this->object = NULL;
	}
	Reference(T* object) {
		this->object = object;
	}
	~Reference() {
		if(this->object) {
			delete this->object;
		}
	}
	operator T*() const {
		return object;
	}
	T* detach() {
		T* t = object;
		object = NULL;
		return t;
	}
};

template <class T> class ReferenceArray {
	T*	object;
public:
	ReferenceArray() {
		this->object = NULL;
	}
	ReferenceArray(T* object) {
		this->object = object;
	}
	~ReferenceArray() {
		if(this->object) {
			delete[] this->object;
		}
	}
	T& operator [](int index) const {
		return object[index];
	}
	T* detach() {
		T* t = object;
		object = NULL;
		return t;
	}
};


// ==========================================================
// TRACE macros
// ==========================================================

#ifdef FULLDEBUG
	#define TRACE0(A)						if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A)}
	#define TRACE1(A,a)						if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a)}
	#define TRACE2(A,a,b)					if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b)}
	#define TRACE3(A,a,b,c)					if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b,c)}
	#define TRACE4(A,a,b,c,d)				if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b,c,d)}
	#define TRACE5(A,a,b,c,d,e)				if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b,c,d,e)}
	#define TRACE6(A,a,b,c,d,e,f)			if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b,c,d,e,f)}
	#define TRACE7(A,a,b,c,d,e,f,g)			if(JNIUtil::FLAG_ENABLE_TRACE){jprintln(A,a,b,c,d,e,f,g)}
#else
	#define TRACE0(A)						
	#define TRACE1(A,a)						
	#define TRACE2(A,a,b)					
	#define TRACE3(A,a,b,c)					
	#define TRACE4(A,a,b,c,d)				
	#define TRACE5(A,a,b,c,d,e)				
	#define TRACE6(A,a,b,c,d,e,f)			
	#define TRACE7(A,a,b,c,d,e,f,g)			
#endif

#ifdef FULLDEBUG
	#define TRACE_FUNCTION(fctName)		TraceFunction(fctName)
	#define TRACE_FUNCTION0(fctName,fmt)									TraceFunction(fctName,fmt)
	#define TRACE_FUNCTION1(fctName,fmt,p1)									TraceFunction(fctName,fmt,p1)
	#define TRACE_FUNCTION2(fctName,fmt,p1,p2)								TraceFunction(fctName,fmt,p1,p2)
	#define TRACE_FUNCTION3(fctName,fmt,p1,p2,p3)							TraceFunction(fctName,fmt,p1,p2,p3)
	#define TRACE_FUNCTION4(fctName,fmt,p1,p2,p3,p4)						TraceFunction(fctName,fmt,p1,p2,p3,p4)
	#define TRACE_FUNCTION5(fctName,fmt,p1,p2,p3,p4,p5)						TraceFunction(fctName,fmt,p1,p2,p3,p4,p5)
	#define TRACE_FUNCTION6(fctName,fmt,p1,p2,p3,p4,p5,p6)					TraceFunction(fctName,fmt,p1,p2,p3,p4,p5,p6)
	#define TRACE_FUNCTION7(fctName,fmt,p1,p2,p3,p4,p5,p6,p7)				TraceFunction(fctName,fmt,p1,p2,p3,p4,p5,p6,p7)
	#define TRACE_FUNCTION8(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8)			TraceFunction(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8)
	#define TRACE_FUNCTION9(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9)			TraceFunction(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9)
	#define TRACE_FUNCTION10(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10)	TraceFunction(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10)
#else
	#define TRACE_FUNCTION(fctName)
	#define TRACE_FUNCTION0(fctName,fmt)									
	#define TRACE_FUNCTION1(fctName,fmt,p1)									
	#define TRACE_FUNCTION2(fctName,fmt,p1,p2)								
	#define TRACE_FUNCTION3(fctName,fmt,p1,p2,p3)							
	#define TRACE_FUNCTION4(fctName,fmt,p1,p2,p3,p4)						
	#define TRACE_FUNCTION5(fctName,fmt,p1,p2,p3,p4,p5)						
	#define TRACE_FUNCTION6(fctName,fmt,p1,p2,p3,p4,p5,p6)					
	#define TRACE_FUNCTION7(fctName,fmt,p1,p2,p3,p4,p5,p6,p7)				
	#define TRACE_FUNCTION8(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8)			
	#define TRACE_FUNCTION9(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9)			
	#define TRACE_FUNCTION10(fctName,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10)	
#endif



#endif
