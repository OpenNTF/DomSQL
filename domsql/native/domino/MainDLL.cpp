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

// DominoSQLLite.cpp : Defines the entry point for the DLL application.
//

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    return TRUE;
}


/////////////////////////////////////////////////////////////////////
// JVM INITIALISATION
/////////////////////////////////////////////////////////////////////

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JNIEnv *env;
    if (JNI_OK != vm->GetEnv((void **)&env, JNI_VERSION))
        return JNI_ERR;

	// Initialize the JNIUtils

	if(!JNIUtils::init(vm,env)) {
		return JNI_ERR;
	}

	// Initialize the DomSQL
	if(!DomSQL::init(env)) {
		return JNI_ERR;
	}

    return JNI_VERSION_1_2;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
}
