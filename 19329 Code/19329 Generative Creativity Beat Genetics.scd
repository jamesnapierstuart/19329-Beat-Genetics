/*
Beat Genetics.
An interactive genetic algorithm system that tailors beats to user's taste.

The fitness function of the genetic algorithm is dependent on the amount of time
the user listens to a beat.
The longer a beat is listened to, the higher its fitness score, and therefore
the more likely it will survive and become a candidate in the mating pool.

With a population size of six, it creates six
randomly generated beat sequences per instrument.
Instruments Include:
kick, snare, closed hi hat, ride, tom, clap, percussion 1 & 2.

//@kickSample = Folder of Kick samples
//@...(rest follow by example)

Part of the Beat Genetics Project.
© 2013-2014, Candidate Number 19329
Generative Creativity, University of Sussex, Spring 2014
*/


// To start BeatGenetics, simply call the Class (And Place folders of samples as arguments not an individual file path (but a folder of samples).

// (For the percussion1 and percussion2 sample paths use the same percussion folder for now, but feel free to add your own)

BeatGenetics("/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Kicks/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Snares/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Close Hats/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Rides/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Toms/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Clap/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Percussion/", "/Users/jamie/Documents/Sussex/University/Music Informatics Year 3/Modules/Term 2/Generative Creativity/Assessments/Programming Project 2.0/Picked Samples/Percussion/");