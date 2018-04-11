package jordan.szalontai.unicrush;

/**
 * Interface for an object that can be transposed.
 *
 * @author Szalontai Jordán
 */
public interface Transposable {

    /**
     * Transposes the object.
     */
    public void transpose();

    /**
     * Returns the desired element of the board.
     *
     * @param i row index
     * @param j column index
     * @return a {@code Candy} instance that is in the board of this level,
     * {@code null} if we give incorrect row or column indexes
     */
    public Candy get(int i, int j);

    /**
     * Sets a {@code Candy} instance in the board.
     *
     * @param i row index
     * @param j column index
     * @param c the new {@code Candy} instance
     * @throws ArrayIndexOutOfBoundsException if we give incorrect row or column
     * indexes
     */
    public void set(int i, int j, Candy c) throws ArrayIndexOutOfBoundsException;
}
