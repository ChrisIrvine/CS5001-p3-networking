/**
 * Class to handle any instances of an unexpected disconnection.
 */
class DisconnectedException extends Exception {

    /**
     * Method that will take and return a message to return that is the source
     * of the error.
     * @param message - exception message
     */
    DisconnectedException(String message) {
        super(message);
    }
}
