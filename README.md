# Gitflow Best Practices

This document outlines the best practices for using the Gitflow workflow in our projects. Gitflow is a branching model designed around the project release. This model helps us manage our projects more efficiently by defining the roles of different branches and when they should be used.

## Branching Strategy

### Main Branches

- `main`/`master`: This branch contains the official release history. Always ensure the code on the `main` branch is in a deployable state.
- `develop`: This is the main development branch containing the next release cycle's code. All feature branches are branched from and merged back into `develop`.

### Supporting Branches

- **Feature Branches**:
  - Branch off from: `develop`
  - Naming convention: `feature/*`
  - Purpose: Used for new feature development and code improvements. Merge back into `develop` when complete.
- **Release Branches**:
  - Branch off from: `develop`
  - Naming convention: `release/*`
  - Purpose: Prepare for a new production release. Allows for minor bug fixes and preparing meta-data for a release. Merge into `main` and `develop` when complete.
- **Hotfix Branches**:
  - Branch off from: `main`
  - Naming convention: `hotfix/*`
  - Purpose: Quickly fix production releases. Merge back into `main` and `develop` upon completion.

## Branch Operations

- Always pull the latest changes from `develop` before starting new work.
- Use Pull Requests (PRs) to merge feature branches into `develop`, ensuring a code review process is in place.
- Prepare the release version in the release branch, then merge it into `main` and `develop`, ensuring `develop` contains all the latest changes.
- After completing a hotfix, merge it back into both `main` and `develop` and release a new patch version immediately.

## Naming Conventions

- Maintain consistency and traceability by following clear naming conventions for branches, commit messages, tags, etc.
- Commit messages should be concise and ideally reference related issue tracker IDs.

## Versioning and Tagging

- Tag every merge into `main` (from release or hotfix branches) with a version number, adhering to [Semantic Versioning (SemVer)](https://semver.org/).

## Code Reviews

- All merges into `develop` and `main` should be done via Pull Requests and reviewed by at least one team member.

## Maintenance

- Regularly review and clean up feature branches that are no longer needed.
- Regularly merge `main` into `develop` to ensure `develop` includes all hotfixes done in production.

## Documentation and Training

- Write a detailed CONTRIBUTING guide, including the branch strategy, naming conventions, and code review process, ensuring all team members understand and follow these best practices.
- Provide Gitflow training for new members to ensure internal consistency.

By adhering to these best practices based on the Gitflow workflow, we aim to manage complex development processes in a structured manner, enhancing collaboration efficiency, reducing conflicts, and ensuring code quality, thereby facilitating the smooth progress of our projects.
