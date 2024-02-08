import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

/**
 * Return specific file.
 */
class TemplateHandler extends FileHandler {
  final Path path;

  
  /** Constructor to create a TemplateHandler
   * @param rootURI The URI to handle
   */
  TemplateHandler(String path) {
    this.path = Paths.get(path);
  }

  /** Method to retrieve a path from an HTTP Exchange
   * @param exchange The HTTP Exchange to retrieve a path from
   */
  @Override
  protected Path getPath(HttpExchange exchange) {
    return path;
  }
}
