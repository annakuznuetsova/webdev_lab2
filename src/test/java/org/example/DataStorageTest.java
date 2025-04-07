package org.example;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataStorageTest {

    @Test
    void testSaveToWriter_writesDataCorrectly() throws IOException {
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        String data = "This is sample data to save in a file!";
        DataStorage.saveToWriter(mockWriter, data);
        verify(mockWriter).write(data);
        verify(mockWriter).newLine();
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    void testSaveToWriter_throwsIOException() throws IOException {
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        doThrow(new IOException("This is sample data to save in a file, but with a trick!")).when(mockWriter).write(anyString());
        assertThrows(IOException.class, () -> DataStorage.saveToWriter(mockWriter, "Oops!"));
    }

    @Test
    void testLoadFromReader_readsAllLines() throws IOException {
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("Line 1")
                .thenReturn("Line 2")
                .thenReturn(null);
        List<String> result = DataStorage.loadFromReader(mockReader);
        assertEquals(Arrays.asList("Line 1", "Line 2"), result);
    }

    @Test
    void testLoadFromReader_returnsEmptyListOnEOF() throws IOException {
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn(null);
        List<String> result = DataStorage.loadFromReader(mockReader);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLoadFromReader_throwsIOException() throws IOException {
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenThrow(new IOException("This is sample data to read from a file, but with a trick!"));
        assertThrows(IOException.class, () -> DataStorage.loadFromReader(mockReader));
    }
}
