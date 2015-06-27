package com.andromeda.client.gl;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


public class GlClient {

  // We need to strongly reference callback instances.
  private GLFWErrorCallback errorCallback;
  private GLFWKeyCallback keyCallback;

  // The window handle
  private long window;

  public void run() {
    System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

    try {
      init();
      loop();

      // Release window and window callbacks
      GLFW.glfwDestroyWindow(window);
      keyCallback.release();
    } finally {
      // Terminate GLFW and release the GLFWerrorfun
      GLFW.glfwTerminate();
      errorCallback.release();
    }
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (GLFW.glfwInit() != GL11.GL_TRUE)
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure our window
    GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,
                        GL11.GL_FALSE); // the window will stay hidden after creation
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE); // the window will be resizable

    int WIDTH = 300;
    int HEIGHT = 300;

    // Create the window
    window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);
    if (window == MemoryUtil.NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
          GLFW.glfwSetWindowShouldClose(window,
                                        GL11.GL_TRUE); // We will detect this in our rendering loop
      }
    });

    // Get the resolution of the primary monitor
    ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    // Center our window
    GLFW.glfwSetWindowPos(
        window,
        (GLFWvidmode.width(vidmode) - WIDTH) / 2,
        (GLFWvidmode.height(vidmode) - HEIGHT) / 2
    );

    // Make the OpenGL context current
    GLFW.glfwMakeContextCurrent(window);
    // Enable v-sync
    GLFW.glfwSwapInterval(1);

    // Make the window visible
    GLFW.glfwShowWindow(window);
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the ContextCapabilities instance and makes the OpenGL
    // bindings available for use.
    GLContext.createFromCurrent();

    verticesBuf = BufferUtils.createFloatBuffer(9);
    verticesBuf.put(vertices);

    vertexBufferId = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuf, GL15.GL_STATIC_DRAW);

    // Set the clear color
    GL11.glClearColor(0.0f, 0.4f, 0.6f, 0.0f);

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE) {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
      GL11.glVertexPointer(3, 0, verticesBuf);
      GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 3);

      GLFW.glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      GLFW.glfwPollEvents();
    }
  }

  private static final float[] vertices = {
      -1.0f, -1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      0.0f, 1.0f, 0.0f
  };
  private static final byte[] colors = {
      (byte) 255, 0, 0, (byte) 255,
      0, (byte) 255, 0, (byte) 255,
      0, 0, (byte) 255, (byte) 255
  };

  private FloatBuffer verticesBuf;
  private ByteBuffer colorsBuf;

  public static void main(String[] args) {
    new GlClient().run();
  }

  private int vertexBufferId;
}
