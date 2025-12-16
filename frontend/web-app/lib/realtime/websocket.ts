import { io, Socket } from "socket.io-client";

/**
 * Cliente WebSocket usando Socket.io para actualizaciones en tiempo real
 */

type MessageHandler = (data: any) => void;

export class WebSocketClient {
  private socket: Socket | null = null;
  private url: string;

  constructor(url: string) {
    this.url = url;
  }

  connect(): void {
    if (this.socket?.connected) {
      console.warn("WebSocket already connected");
      return;
    }

    this.socket = io(this.url, {
      transports: ["websocket"],
      reconnection: true,
      reconnectionAttempts: 5,
      reconnectionDelay: 1000,
    });

    this.socket.on("connect", () => {
      console.log("WebSocket connected");
    });

    this.socket.on("disconnect", () => {
      console.log("WebSocket disconnected");
    });

    this.socket.on("error", (error) => {
      console.error("WebSocket error:", error);
    });
  }

  on(event: string, handler: MessageHandler): void {
    if (!this.socket) {
      console.error("WebSocket not initialized");
      return;
    }
    this.socket.on(event, handler);
  }

  off(event: string, handler?: MessageHandler): void {
    if (!this.socket) {
      console.error("WebSocket not initialized");
      return;
    }
    this.socket.off(event, handler);
  }

  emit(event: string, data: any): void {
    if (!this.socket) {
      console.error("WebSocket not initialized");
      return;
    }
    this.socket.emit(event, data);
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.disconnect();
      this.socket = null;
      console.log("WebSocket closed");
    }
  }

  isConnected(): boolean {
    return this.socket?.connected || false;
  }

  // Métodos específicos para partidos en vivo
  subscribeToMatch(matchId: number): void {
    this.emit("subscribe:match", { matchId });
  }

  unsubscribeFromMatch(matchId: number): void {
    this.emit("unsubscribe:match", { matchId });
  }

  subscribeToLiveMatches(): void {
    this.emit("subscribe:live-matches", {});
  }

  unsubscribeFromLiveMatches(): void {
    this.emit("unsubscribe:live-matches", {});
  }
}

// Factory function para crear clientes WebSocket
export function createWebSocketClient(): WebSocketClient {
  const wsUrl = process.env.NEXT_PUBLIC_WS_URL || "ws://localhost:8080";
  return new WebSocketClient(wsUrl);
}
