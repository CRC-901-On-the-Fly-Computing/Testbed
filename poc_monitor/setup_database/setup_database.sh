#!/bin/sh


run() {
	isInstalled curl


	# change to directory of script
	oldDir=`pwd`
	cd "${0%/*}"

	# load urls
	. "./urls.sh"

	. "./create_pipelines.sh"

	# change back to initial directory
	cd "${oldDir}"
}


isInstalled() {
	command=$1

	unset commandFound
	which ${command} 2>&1 >/dev/null && commandFound=true

	if [ -z "${commandFound:-}" ]
	then
		echo "Command '${command}' not found. It has to be installed." 1>&2
		exit 1
	fi
}


run

