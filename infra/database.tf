resource "kubernetes_secret" "postgres" {
  metadata {
    name = "garage-postgresql-secret"
  }
  data = {
    POSTGRES_DB       = var.db_name
    POSTGRES_USER      = var.db_user
    POSTGRES_PASSWORD  = var.db_password
  }
  depends_on = [kind_cluster.this]
}

resource "kubernetes_persistent_volume_claim" "postgres" {
  metadata {
    name = "garage-postgresql-pvc"
  }
  wait_until_bound = false
  spec {
    access_modes = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = var.db_storage_size
      }
    }
  }
  depends_on = [kind_cluster.this]
}

resource "kubernetes_deployment" "postgres" {
  metadata {
    name   = "garage-postgresql"
    labels = { app = "garage-postgresql" }
  }

  spec {
    replicas = 1

    selector {
      match_labels = { app = "garage-postgresql" }
    }

    template {
      metadata {
        labels = { app = "garage-postgresql" }
      }

      spec {
        container {
          name  = "postgres"
          image = "postgres:16-alpine"

          port {
            container_port = 5432
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.postgres.metadata[0].name
            }
          }

          volume_mount {
            name       = "data"
            mount_path = "/var/lib/postgresql/data"
            sub_path   = "postgres"
          }

          readiness_probe {
            exec {
              command = ["pg_isready", "-U", var.db_user]
            }
            initial_delay_seconds = 10
            period_seconds         = 5
          }

          liveness_probe {
            exec {
              command = ["pg_isready", "-U", var.db_user]
            }
            initial_delay_seconds = 20
            period_seconds         = 10
          }
        }

        volume {
          name = "data"
          persistent_volume_claim {
            claim_name = kubernetes_persistent_volume_claim.postgres.metadata[0].name
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "postgres" {
  metadata {
    name = "garage-postgresql"
  }
  spec {
    selector = { app = "garage-postgresql" }
    port {
      port        = 5432
      target_port = 5432
    }
  }
}