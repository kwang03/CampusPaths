### Written Answers: 24/26

- Immutability is a property of the specification, and `checkRep` does not assume
the specification was correctly implemented.  So, in general, regardless of
whether or not they are immutable, ADTs need calls to `checkRep` at the
beginning and end of all public methods.

- For alternate representation, the advantage would be that using RatPoly would 
be easier since the client doesn't need to understand RatTerm.

### Code Quality: 3/3

### Mechanics: 3/3

### General Feedback

Great work overall, make sure to add in checkRep calls and clean up comments. 

### Specific Feedback

- Please remove TODO after implementing the methods.

- Missing calls to `checkRep` at the beginning and end of every public method in `RatPoly` and/or `RatPolyStack`.

