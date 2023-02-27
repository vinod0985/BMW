#!/bin/bash
repo_root="$(git rev-parse --show-toplevel)"
cd $repo_root/build/libs
cp -rf swgwda-0.0.1.jar $repo_root/docker/

docker build -f "${repo_root}/docker/Dockerfile" -t armdocker.rnd.ericsson.se/proj-swgw-da-drop/swgwda:0.0.3 --rm .