class CommentTestInput {
  private int sampleField = 0;
  private String anotherSampleField;

  /**
   * Sample javadoc comment
   *
   * @return sample field
   */
  public int getSampleField() {
    /*
    Sample multiline



        return sampleFieldB;
     */
    return sampleField;
  }

  public String getAnotherSampleField() {
    return this.anotherSampleField;
  }

  public void setSampleField(int sampleField) {

    // Sample comment single-line

    this.sampleField = sampleField;
  }
}
