NAMES: David T. Kraft  Yohannes M. Himawan
UT EIDS: dtk265 ymh79
SECTIONS: 54195 54185

CODING STATUS: WORKING PERFECTLY

Our RK function currently uses a rolling hash function. It mods by a value P. P is a constant defined at the
beginning of the strMatch class.

Both search functions use a buffer. Since we have to make several reads to the file and change what's in our
buffer, our string indices change every time we read in. So our search functions require an input file to be
provided in order to run.

We currently have a buffer size of 2000. Since Nam gave 1000 as an example buffer size, we assume that no
pattern will be as big as 1000. Therefore we use a buffer size of 2000 and fill it in 1000 characters at a time.
