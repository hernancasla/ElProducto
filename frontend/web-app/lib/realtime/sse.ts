/**
 * Cliente SSE (Server-Sent Events) para recibir actualizaciones en tiempo real
 */

type SSEMessageHandler = (data: any) => void;
type SSEErrorHandler = (error: Event) => void;

export class SSEClient {
  private eventSource: EventSource | null = null;
  private url: string;
  private messageHandlers: Map<string, SSEMessageHandler[]> = new Map();
  private errorHandler: SSEErrorHandler | null = null;

  constructor(url: string) {
    this.url = url;
  }

  connect(): void {
    if (this.eventSource) {
      console.warn("SSE already connected");
      return;
    }

    this.eventSource = new EventSource(this.url);

    this.eventSource.onopen = () => {
      console.log("SSE connection established");
    };

    this.eventSource.onerror = (error) => {
      console.error("SSE error:", error);
      if (this.errorHandler) {
        this.errorHandler(error);
      }
    };

    // Manejar eventos personalizados
    this.messageHandlers.forEach((handlers, eventType) => {
      handlers.forEach((handler) => {
        this.eventSource?.addEventListener(eventType, (event: MessageEvent) => {
          try {
            const data = JSON.parse(event.data);
            handler(data);
          } catch (error) {
            console.error("Error parsing SSE message:", error);
          }
        });
      });
    });
  }

  on(eventType: string, handler: SSEMessageHandler): void {
    if (!this.messageHandlers.has(eventType)) {
      this.messageHandlers.set(eventType, []);
    }
    this.messageHandlers.get(eventType)?.push(handler);

    // Si ya está conectado, agregar el listener
    if (this.eventSource) {
      this.eventSource.addEventListener(eventType, (event: MessageEvent) => {
        try {
          const data = JSON.parse(event.data);
          handler(data);
        } catch (error) {
          console.error("Error parsing SSE message:", error);
        }
      });
    }
  }

  onError(handler: SSEErrorHandler): void {
    this.errorHandler = handler;
  }

  disconnect(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      console.log("SSE connection closed");
    }
  }

  isConnected(): boolean {
    return this.eventSource !== null && this.eventSource.readyState === EventSource.OPEN;
  }
}

// Factory function para crear clientes SSE
export function createSSEClient(endpoint: string): SSEClient {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1";
  return new SSEClient(`${baseUrl}${endpoint}`);
}
