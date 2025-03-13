package com.example.opticyou.communications

class ReturnValues : EndPointData {
    /**
     * Gets the response's return code.
     * @return return code
     */
    var returnCode: Int = 0
        private set


    /**
     * Default constructor
     */
    constructor() : super()

    /**
     * Constructor of a ReturnValue that represents a response with the specified return code without more data.These can be filled later.
     * @param returnCode return code
     */
    constructor(returnCode: Int) : super() {
        this.returnCode = returnCode
    }
}
