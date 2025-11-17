Project 3: C Semantic Analysis and Activation Records

Team Members:
David Sandoval
Jonathan DeRose
Hunter D'Arensbourg
Sean Pham

Our project successfully implements semantic analysis and activation record construction. We had issues with the semantic rules but when we noticed that one of the chapters already had the full semantic rules already in tiger we were able to make it work with C.  When making the traverse functions we used some AI tools to help but it was not as difficult to implement in comparison to the rest. The memory allocation was the hardest to implement because we kept on getting random errors that were difficult to debug, but, with the help of online resources and AI tools like Claude and ChatGPT, we were able to get them resolved. We found the AI tools to be extremely helpful in this project. Using the already made Tiger code made this project a little easier since we can somewhat gauge what we needed to change to make it work for C. The semantic analyzer applies C's typing rules, handles variables, functions, pointers, and expressions. Escape analysis identifies variables of addresses that are taken, and activation records are made using C-style frames without static links. The updated data structures and frame allocations are accurate to how C calls functions and its storage requirements. 
