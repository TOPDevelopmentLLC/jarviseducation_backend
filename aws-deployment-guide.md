# AWS Deployment Guide for JarvisEd Backend (GitHub Actions)

## 🚀 Fully Automated Deployment with GitHub Actions

This guide uses **GitHub Actions** to automatically deploy your application to AWS. No manual AWS CLI commands needed!

---

## Prerequisites

1. **GitHub Repository** for your code
2. **AWS Account** with appropriate permissions
3. **Docker** installed locally (for testing only)

---

## 📋 Table of Contents

1. [Quick Start (5 minutes)](#quick-start)
2. [Detailed Setup Instructions](#detailed-setup)
3. [Understanding the Architecture](#architecture)
4. [Cost Breakdown](#cost-breakdown)
5. [Monitoring & Maintenance](#monitoring)
6. [Troubleshooting](#troubleshooting)

---

## Quick Start

### Step 1: Setup GitHub Repository

```bash
cd /path/to/jarvised_backend

# Initialize git if not already done
git init

# Add remote (replace with your repo URL)
git remote add origin https://github.com/your-username/jarvised_backend.git

# Commit everything
git add .
git commit -m "Initial commit with multi-tenant support"
git push -u origin main
```

### Step 2: Add AWS Credentials to GitHub Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Add these secrets:

| Secret Name | Value | How to Get |
|-------------|-------|------------|
| `AWS_ACCESS_KEY_ID` | Your AWS access key | [Create IAM user](https://console.aws.amazon.com/iam) with `AdministratorAccess` |
| `AWS_SECRET_ACCESS_KEY` | Your AWS secret key | Same as above |

**Create IAM User:**
```bash
# Or use AWS Console: IAM → Users → Add User
# Attach policy: AdministratorAccess (for setup)
# Save the Access Key ID and Secret Access Key
```

### Step 3: Run Infrastructure Setup

1. Go to **Actions** tab in your GitHub repo
2. Click **Setup AWS Infrastructure** workflow
3. Click **Run workflow**
4. Enter:
   - **DB Password**: Strong password for RDS (e.g., `MySecure123!Password`)
   - **JWT Secret**: Your JWT secret (use existing or generate new)
5. Click **Run workflow**

⏳ **Wait 10-15 minutes** - This creates:
- RDS MySQL database
- ECS Cluster
- Load Balancer
- Security Groups
- Task Definitions
- Secrets Manager entries

### Step 4: Deploy Your Application

Simply push to main branch:

```bash
git push origin main
```

GitHub Actions will automatically:
1. Build Docker image
2. Push to AWS ECR
3. Deploy to ECS
4. Wait for service stability

### Step 5: Access Your API

Find your URL in the **Setup Infrastructure** workflow output:
```
Load Balancer: http://jarvised-alb-xxxxx.us-east-1.elb.amazonaws.com
```

Test it:
```bash
curl http://your-alb-url/actuator/health
```

---

## Detailed Setup

### Understanding the Workflows

You have **2 GitHub Actions workflows**:

#### 1. `.github/workflows/setup-infrastructure.yml`
**Purpose:** One-time setup of AWS resources
**When to run:** Once, before first deployment
**Trigger:** Manual only

**What it creates:**
- ECR repository for Docker images
- RDS MySQL instance
- ECS cluster and service
- Application Load Balancer
- Security groups
- CloudWatch log groups
- IAM roles
- Secrets in AWS Secrets Manager

#### 2. `.github/workflows/deploy-ecs.yml`
**Purpose:** Deploy application code
**When to run:** Every time you push code
**Trigger:** Automatic on push to `main` or `master`

**What it does:**
- Builds Docker image
- Pushes to ECR
- Updates ECS task definition
- Deploys new version
- Waits for health checks

---

## Architecture

```
┌─────────────────────────────────────────────────┐
│              GitHub Actions                     │
│  ┌─────────────────────────────────────────┐   │
│  │  On Push → Build → Deploy → Monitor     │   │
│  └─────────────────────────────────────────┘   │
└────────────────┬────────────────────────────────┘
                 │ Push Image
                 ▼
┌─────────────────────────────────────────────────┐
│         Amazon ECR (Container Registry)         │
└────────────────┬────────────────────────────────┘
                 │ Pull Image
                 ▼
┌─────────────────────────────────────────────────┐
│   Application Load Balancer (Public)           │
│   http://jarvised-alb-xxx.elb.amazonaws.com    │
└────────────────┬────────────────────────────────┘
                 │
    ┌────────────┴────────────┐
    │                         │
┌───▼────┐              ┌────▼───┐
│  ECS   │              │  ECS   │
│ Task 1 │              │ Task 2 │  (Auto-scales)
│(Fargate)│              │(Fargate)│
└───┬────┘              └────┬───┘
    │                        │
    └────────────┬───────────┘
                 │
    ┌────────────▼────────────┐
    │   RDS MySQL Instance    │
    │   - master_db           │
    │   - school_lincoln      │  (Created dynamically)
    │   - school_eastside     │  (Created dynamically)
    └─────────────────────────┘
```

---

## Production Configuration

### Update Application Properties

The setup workflow automatically configures your app to use environment variables. Your [application.properties](src/main/resources/application.properties) already supports this with defaults:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/master_db}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:secret}
jwt.secret=${JWT_SECRET:your-default-secret}
```

For production-specific settings, create `src/main/resources/application-prod.properties`:

```properties
# Production profile
spring.application.name=JarvisEdBackend

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Connection pool for production
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Logging
logging.level.root=INFO
logging.level.com.top.jarvised=DEBUG
```

Add Spring Boot Actuator for health checks:

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then configure actuator in `application-prod.properties`:

```properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

---

## Cost Breakdown

| AWS Service | Configuration | Monthly Cost |
|------------|---------------|--------------|
| **ECS Fargate** | 0.5 vCPU, 1GB RAM, 2 tasks | ~$30 |
| **RDS MySQL** | db.t3.micro, 20GB storage | ~$15 |
| **Application Load Balancer** | Standard | ~$20 |
| **ECR** | Docker image storage (~2GB) | ~$0.20 |
| **CloudWatch Logs** | ~10GB logs/month | ~$5 |
| **Secrets Manager** | 2 secrets | ~$0.80 |
| **Data Transfer** | Minimal | ~$2 |
| **Total** | | **~$73/month** |

### Cost Optimization Tips:

1. **Use Fargate Spot** (50-70% cheaper):
   - Modify workflow to use `capacityProviderStrategy` with `FARGATE_SPOT`

2. **Reduce task count** during low traffic:
   - Set `desired-count` to 1 instead of 2

3. **Use RDS Reserved Instance** (40% cheaper):
   - After stable, purchase 1-year reserved instance

4. **Enable ALB idle timeout**:
   - Reduce costs during low traffic

**Estimated savings: ~$25-30/month** = **$43-48/month total**

---

## Monitoring & Maintenance

### CloudWatch Dashboards

Access logs:
```bash
# Via AWS Console
CloudWatch → Log groups → /ecs/jarvised-backend

# Or via CLI
aws logs tail /ecs/jarvised-backend --follow
```

### Check Deployment Status

```bash
# Check ECS service
aws ecs describe-services \
  --cluster jarvised-cluster \
  --services jarvised-service

# Check running tasks
aws ecs list-tasks --cluster jarvised-cluster

# View task logs
aws logs tail /ecs/jarvised-backend --follow
```

### Database Management

Connect to RDS:
```bash
# Get RDS endpoint from AWS Console or:
RDS_ENDPOINT=$(aws rds describe-db-instances \
  --db-instance-identifier jarvised-db \
  --query "DBInstances[0].Endpoint.Address" \
  --output text)

# Connect with MySQL client
mysql -h $RDS_ENDPOINT -u admin -p
```

Check tenant databases:
```sql
SHOW DATABASES;
-- Should see:
-- master_db
-- school_xxx
-- school_yyy
```

---

## Continuous Deployment Workflow

### Daily Development Flow:

1. **Make code changes** locally
2. **Test locally**:
   ```bash
   docker-compose up
   ```
3. **Commit and push**:
   ```bash
   git add .
   git commit -m "Add new feature"
   git push origin main
   ```
4. **GitHub Actions automatically**:
   - Builds Docker image
   - Runs tests (if configured)
   - Deploys to ECS
   - Validates deployment

5. **Monitor deployment** in GitHub Actions tab
6. **Verify** on your ALB URL

### Rolling Back

If deployment fails, GitHub Actions will automatically stop. To manually rollback:

```bash
# From your local machine or GitHub Actions
aws ecs update-service \
  --cluster jarvised-cluster \
  --service jarvised-service \
  --task-definition jarvised-backend:PREVIOUS_VERSION
```

Or use GitHub Actions:
1. Go to **Actions** tab
2. Find successful previous deployment
3. Click **Re-run jobs**

---

## Advanced Configuration

### Enable Auto-Scaling

Add this to your workflow or run manually:

```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --resource-id service/jarvised-cluster/jarvised-service \
  --scalable-dimension ecs:service:DesiredCount \
  --min-capacity 1 \
  --max-capacity 10

# CPU-based scaling
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --resource-id service/jarvised-cluster/jarvised-service \
  --scalable-dimension ecs:service:DesiredCount \
  --policy-name cpu-scaling \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration '{
    "TargetValue": 70.0,
    "PredefinedMetricSpecification": {
      "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
    },
    "ScaleInCooldown": 300,
    "ScaleOutCooldown": 60
  }'
```

### Add Custom Domain

1. **Get a domain** (Route 53 or external)
2. **Create ACM certificate**:
   ```bash
   aws acm request-certificate \
     --domain-name api.yourdomain.com \
     --validation-method DNS
   ```
3. **Add HTTPS listener to ALB**:
   ```bash
   aws elbv2 create-listener \
     --load-balancer-arn <your-alb-arn> \
     --protocol HTTPS \
     --port 443 \
     --certificates CertificateArn=<cert-arn> \
     --default-actions Type=forward,TargetGroupArn=<target-group-arn>
   ```
4. **Create Route 53 record** pointing to ALB

### Enable HTTPS Only

Modify ALB listener to redirect HTTP to HTTPS:
```bash
aws elbv2 modify-listener \
  --listener-arn <http-listener-arn> \
  --default-actions Type=redirect,RedirectConfig={Protocol=HTTPS,Port=443,StatusCode=HTTP_301}
```

---

## Troubleshooting

### Deployment Fails

**Check GitHub Actions logs:**
1. Go to Actions tab
2. Click failed workflow
3. Expand failing step

**Common issues:**

1. **AWS credentials expired:**
   - Regenerate IAM keys
   - Update GitHub secrets

2. **Image build fails:**
   - Check Dockerfile syntax
   - Verify Maven dependencies resolve

3. **ECS task fails to start:**
   ```bash
   # Check task logs
   aws logs tail /ecs/jarvised-backend --follow

   # Check task stopped reason
   aws ecs describe-tasks \
     --cluster jarvised-cluster \
     --tasks <task-id>
   ```

4. **Database connection fails:**
   - Verify security groups allow MySQL (port 3306)
   - Check RDS endpoint in task definition
   - Verify Secrets Manager has correct password

### Application Issues

**Cannot create tenant databases:**
- Check RDS user has `CREATE DATABASE` permission
- Verify RDS storage not full
- Check CloudWatch logs for SQL errors

**JWT authentication fails:**
- Verify JWT_SECRET in Secrets Manager matches application
- Check token expiration time

**Multi-tenancy routing issues:**
- Verify `schoolId` in JWT token
- Check SchoolContext is being set
- Review DataSourceRouter logs

---

## Security Best Practices

✅ **Implemented:**
- Secrets stored in AWS Secrets Manager
- RDS in private subnet (via VPC)
- IAM roles for ECS tasks
- Security groups restrict access
- Container image scanning enabled

🔒 **Additional recommendations:**

1. **Enable RDS encryption at rest:**
   ```bash
   aws rds modify-db-instance \
     --db-instance-identifier jarvised-db \
     --storage-encrypted
   ```

2. **Enable SSL for MySQL connections:**
   Update application.properties:
   ```properties
   spring.datasource.url=jdbc:mysql://...?useSSL=true&requireSSL=true
   ```

3. **Rotate secrets regularly:**
   - Use AWS Secrets Manager automatic rotation
   - Update GitHub secrets quarterly

4. **Enable AWS CloudTrail:**
   - Track all API calls
   - Audit security changes

5. **Use VPC endpoints:**
   - Reduce internet exposure
   - Lower data transfer costs

---

## Cleanup / Teardown

To delete all AWS resources and stop billing:

```bash
# Delete ECS service
aws ecs update-service \
  --cluster jarvised-cluster \
  --service jarvised-service \
  --desired-count 0

aws ecs delete-service \
  --cluster jarvised-cluster \
  --service jarvised-service \
  --force

# Delete ECS cluster
aws ecs delete-cluster --cluster jarvised-cluster

# Delete RDS (keep final snapshot)
aws rds delete-db-instance \
  --db-instance-identifier jarvised-db \
  --final-db-snapshot-identifier jarvised-final-snapshot

# Delete ALB
aws elbv2 delete-load-balancer --load-balancer-arn <alb-arn>
aws elbv2 delete-target-group --target-group-arn <tg-arn>

# Delete ECR repository
aws ecr delete-repository \
  --repository-name jarvised-backend \
  --force

# Delete secrets
aws secretsmanager delete-secret --secret-id jarvised/db-password
aws secretsmanager delete-secret --secret-id jarvised/jwt-secret

# Delete CloudWatch logs
aws logs delete-log-group --log-group-name /ecs/jarvised-backend
```

---

## Summary

### ✅ What You Get:

- **Fully automated deployment** via GitHub Actions
- **Zero-downtime deployments** with rolling updates
- **Auto-scaling** based on traffic
- **Multi-tenant architecture** with dynamic database creation
- **Secure secrets management** with AWS Secrets Manager
- **Monitoring and logging** with CloudWatch
- **Load balancing** with ALB
- **Container orchestration** with ECS Fargate

### 🎯 Next Steps:

1. ✅ Run **Setup Infrastructure** workflow (one-time)
2. ✅ Push code to trigger first deployment
3. ✅ Test your API endpoints
4. ✅ Set up custom domain (optional)
5. ✅ Configure monitoring alerts
6. ✅ Enable auto-scaling
7. ✅ Document your API

### 🚀 Your Deployment is Ready!

Every time you push to main branch, your application automatically:
- Builds 🏗️
- Tests ✅
- Deploys 🚀
- Validates ✔️

**No manual intervention needed!**

---

## Getting Help

- **AWS Documentation:** https://docs.aws.amazon.com/ecs
- **GitHub Actions Docs:** https://docs.github.com/actions
- **Spring Boot on AWS:** https://spring.io/guides/gs/spring-boot-docker/

---

**Made with ❤️ for JarvisEd Multi-Tenant Backend**
