az account set --subscription "ae4dfe27-72ed-4297-abc7-e189ae9cebfe"

az group create -l eastus -n rg-vm-challenge

az vm create \
  --resource-group rg-vm-challenge \
  --name vm-challenge \
  --image Canonical:ubuntu-24_04-lts:minimal:24.04.202505020 \
  --size Standard_B2s \
  --admin-username admin_fiap \
  --admin-password admin_fiap@123 \
  --public-ip-sku Standard

az network nsg rule create \
  --resource-group rg-vm-challenge \
  --nsg-name vm-challengeNSG \
  --name port_8080 \
  --protocol tcp \
  --priority 1010 \
  --destination-port-range 8080

az network nsg rule create \
  --resource-group rg-vm-challenge \
  --nsg-name vm-challengeNSG \
  --name port_80 \
  --protocol tcp \
  --priority 1020 \
  --destination-port-range 80
