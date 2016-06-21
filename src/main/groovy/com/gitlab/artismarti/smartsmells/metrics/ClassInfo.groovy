package com.gitlab.artismarti.smartsmells.metrics

import com.gitlab.artismarti.smartsmells.common.Smelly
import com.gitlab.artismarti.smartsmells.common.source.SourcePath
import com.gitlab.artismarti.smartsmells.common.source.SourceRange
import groovy.transform.Immutable
import groovy.transform.ToString

/**
 * @author artur
 */
@Immutable
@ToString(includePackage = false, includeNames = true)
class ClassInfo implements Smelly {
	String name
	String signature
	int wmc
	double tcc
	int atfd
	int noa
	int nom
	int loc
	int sloc
	@Delegate
	SourcePath sourcePath
	@Delegate
	SourceRange sourceRange
}
