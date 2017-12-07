package com.eluleci.bux.data.source.remote

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

// used to identify the remote stores
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class Remote
