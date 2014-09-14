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

#include "allinc.h"


////////////////////////////////////////////////////////////////////////////////////////////////////////
// Error handling
////////////////////////////////////////////////////////////////////////////////////////////////////////

void ThrowErrorRelease( JNIEnv *env, const char* msg, STATUS err ) {
    // Clear the pending exception status
    env->ExceptionDescribe();
    env->ExceptionClear();
	
	// Use a custom constructor
	jstring jmsg = env->NewStringUTF(msg);
	jthrowable throwable = (jthrowable)JNIUtils::createException(env,jmsg);
	env->Throw(throwable);
}

bool NCheck( JNIEnv *env, STATUS err ) {
    if( err!=NOERROR ) {
        // Get the notes error msg
        char buffer[512];
        OSLoadString( NULLHANDLE, (STATUS)err, buffer, WORD(sizeof(buffer))-1 );

		jint errorCode = (jint)err;
		ThrowErrorRelease(env,buffer,err);

		jprintln(buffer);
		return false;
     }

     return true;
}

void NPrintError( JNIEnv *env, STATUS err ) {
    if( err!=NOERROR ) {
        // Get the notes error msg
        char buffer[512];
        OSLoadString( NULLHANDLE, (STATUS)err, buffer, WORD(sizeof(buffer))-1 );

		jint errorCode = (jint)err;

		jprintln(buffer);
     }
}



////////////////////////////////////////////////////////////////////////////////////////////////////////
// String helpers
////////////////////////////////////////////////////////////////////////////////////////////////////////


jstring LMBCStoJavaString(JNIEnv *env, const char *str)
{
	if(str) {
		return LMBCStoJavaString(env,str, 0);
	}
	return NULL;
}

jstring LMBCStoJavaString(JNIEnv *env, const char *str, int len)
{
	if(str) {
		if (!*str) {
			len = 0;
		}

		WORD pSize = len == 0 ? SIZET_TO_WORD(strlen(str)) : len;
		jchar localBuffer[2048];
		jchar* unicodeBuffer;
		jstring jStr = NULL;

		if (sizeof(jchar)*(pSize+1) < sizeof (localBuffer)) {
			unicodeBuffer = localBuffer;
			memset (localBuffer, 0, sizeof (localBuffer));
		} else {
			unicodeBuffer = (jchar*)MemAlloc(sizeof(jchar)*(pSize+1));
		}

		if (unicodeBuffer != NULL) {
			pSize = OSTranslate(OS_TRANSLATE_LMBCS_TO_UNICODE,str,pSize,(char*)unicodeBuffer,((pSize + 1) * sizeof (jchar)));
			jStr = env->NewString(unicodeBuffer, pSize/sizeof(jchar));
			if (unicodeBuffer != localBuffer) {
				MemFree(unicodeBuffer);
			}
		}
		return jStr;
	}

	return NULL;
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jstring s)
{
	int length = s !=NULL ? env->GetStringLength(s) : 0;
	if(length>0) {
		const jchar* c = (jchar*)env->GetStringCritical( s, NULL );
		init(env,c,length);
		// Free the temporary java buffer
		env->ReleaseStringCritical(s, c);
	} else {
	}
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jchar* c) {
	int length = c !=NULL ? jstrlen(c) : 0;
	init(env,c,length);
}

JStringLMBCS::JStringLMBCS(JNIEnv* env, jchar* c, int length) {
	init(env,c,length);
}


void JStringLMBCS::init(JNIEnv* env, const jchar* jc, int length) {
	this->data = NULL;
	if(length > 0) {
		// Allocate the buffer that will receive the LMBCS characters
		// *3 is safe enough, but the actual string will certainly be shorter
		WORD pSize = length*3;
		if (sizeof(char)*(pSize+1) < sizeof (localBuffer)) {
			data = localBuffer;
			memset (localBuffer, 0, sizeof (localBuffer));
		} else {
			data = (char*)MemAlloc(sizeof(char)*(pSize+1));
		}

		if (data != NULL) {
			pSize = OSTranslate(OS_TRANSLATE_UNICODE_TO_LMBCS,(const char*)jc,length*sizeof(jchar),(char*)data,((pSize + 1) * sizeof (char)));
			data[pSize] = 0;
		}
	}
}



/*Reference date for calculating java-like date*/
struct DateTimeUtil {
	TIME	refDate;
	DateTimeUtil() {
		memset(&refDate, sizeof(refDate), 0);
		//refDate.second = 0;
		//refDate.minute = 0;
		//refDate.hour = 0;
		refDate.day = 1;
		refDate.month = 1;
		refDate.year = 1970;
		::TimeLocalToGM(&refDate);
	}
};

static DateTimeUtil dateTimeUtil;

jlong convertTIMEDATEtoJavaMilliSeconds(TIMEDATE& dt) {
	jlong l = TimeDateDifference(&dt,&dateTimeUtil.refDate.GM);
	return l*1000;
}

jlong convertTIMEDATEtoJavaSeconds(TIMEDATE& dt) {
	jlong l = TimeDateDifference(&dt,&dateTimeUtil.refDate.GM);
	return l;
}
