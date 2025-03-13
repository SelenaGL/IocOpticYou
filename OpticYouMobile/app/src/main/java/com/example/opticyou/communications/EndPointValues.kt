package com.example.opticyou.communications


/**
 * Representation of the order to be sent to the server
 * @author professor
 */
class EndPointValues : EndPointData {
    /**
     * gets the order
     * @return order
     */
    var order: String? = null
        private set


    /**
     * Default constructor
     */
    constructor() : super()

    /**
     * Constructor of an EndPoint that represents the specified order without parameters. These can be filled later.
     * @param order represented order
     */
    constructor(order: String?) : super() {
        this.order = order
    }
}
