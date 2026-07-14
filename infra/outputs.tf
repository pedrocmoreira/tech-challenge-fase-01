output "cluster_name" {
  value = kind_cluster.this.name
}

output "kubeconfig_path" {
  value = kind_cluster.this.kubeconfig_path
}

output "cluster_endpoint" {
  value = kind_cluster.this.endpoint
}

output "postgres_service" {
  value = "garage-postgresql:5432"
}