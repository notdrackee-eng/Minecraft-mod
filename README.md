# ModMenu Manager — Fabric 1.21.11

This repository is a **development prototype**, not a finished release.

## Build it on GitHub

1. Sign in to GitHub and create a new repository. You can call it `modmenu-manager`.
2. Keep it **Public** or **Private** — either works.
3. On the new repository page, choose **Add file → Upload files**.
4. Extract this ZIP on your computer. Open the `modmenu-manager` folder inside it.
5. Upload the *contents* of that folder (including the hidden `.github` folder) to the repository's main page. Do not upload the outer ZIP as a single file.
6. Commit the files.
7. Open the repository's **Actions** tab. Select **Build ModMenu Manager** on the left.
8. Click **Run workflow** if it has not started automatically.
9. Wait for the run to finish. A green check means the Gradle project compiled.
10. Open the completed run, scroll to **Artifacts**, and download `modmenu-manager-build`. Extract it to get the `.jar`.

## Install

Only install the JAR if the GitHub build succeeds. Put the built JAR in the `mods` folder for your Fabric 1.21.11 instance. Keep Fabric Loader and Fabric API installed.

## Important feature status

The current Java source contains a basic manager screen, a period (`.`) keybind, and JSON preference storage. **It does not yet implement the hooks that actually hide mods from Mod Menu or hide keybinds from Controls.** A successful GitHub build only confirms that this prototype compiles; it does not mean the requested hiding features work. Do not treat this build as the finished ModMenu Manager.
