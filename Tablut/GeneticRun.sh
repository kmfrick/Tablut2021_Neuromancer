#!/bin/bash
BRAINMATES_PATH="/Users/kmfrick/Documents/Code/Tablut2020_BrAInmates/jars/BrAInmates.jar"
NEUROGENETIC_PATH="/Users/kmfrick/Documents/Code/GeneticAlgorithmNeuromancer"
TIMEOUT=1

play() {
	echo "Removing game_start${1}"
	rm /tmp/game_start${1}
	rm logs/*
	java -jar jar/server.jar  &
	sleep 4
	java -jar jar/neurowhite.jar ${TIMEOUT} > white.log &
	java -jar ${BRAINMATES_PATH} black ${TIMEOUT} localhost > black.log
	sleep 4
	cat logs/*Neuromance* | wc -l | tr -d ' ' >> /tmp/NeuroClientOutput.txt
	echo "Creating genetic_start${1}"
	touch /tmp/genetic_start${1}
}

if (( $# != 1 )); then
    >&2 echo "Please specify process ID"
fi

ID=${1}

it=1
killall java # Dangerous :)
rm genetic.log black.log white.log server.log
play ${ID}
java -cp ${NEUROGENETIC_PATH} MainGeneticAlgorithm  0  WHITE  &
while [[ 1 -eq 1 ]]
do
	echo "Waiting for /tmp/game_start${ID}"
	while [[  ! -f /tmp/game_start${ID} ]]  
	do
		sleep 1
	done
	echo "Iteration ${it}"
	play ${ID}
	it=$(expr ${it} + 1)
done

