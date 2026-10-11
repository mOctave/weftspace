# Contributing to Weftspace

Thank you for showing interest in my little project! I'd love your help in making this library more powerful and reliable.

## Issues

The [Issues](https://github.com/mOctave/weftspace/issues/) page is intended for posting bug reports and feature requests. When you post a new issue, please:
- Check to make sure it's not a duplicate of an existing issue
- Complete one issue per bug or feature you want to bring up
- Choose the proper template, and fill it out completely

If you're not sure whether something belongs in Issues or Discussions, don't worry too much about it. However, if it's isn't a bug report or feature request,
you should probably head over to [Discussions](https://github.com/mOctave/weftspace/discussions/) to post it instead.

## Pull Requests

Pull Requests are the most direct form of contributing to Weftspace. Feel free to PR any documentation fixes, code, or tests that you think would be helpful
for this project, so long as it follows the policies listed in this section.

When you PR your work, you relinquish sole ownership of it. Code—or anything else—you PR may be added, modified, or removed by any future contributor. If your
code is removed, you *will* still retain credit for your contribution to the file it was a part of.

Before you hurry to post your PR, please check the following:
- All tests are passing on your machine (use `mvn test`, `pytest tests/__init__.py`, etc.)
- The dif contains no accidental or unrelated changes
- All methods and fields have proper, helpful doc comments
- All indentation has been done with tabs, except for in XML files where two spaces should be used
- Lines wrap at a reasonable point (no hard limit, but please don't PR a 3000-char line of code)
- All other normal programming conventions are followed, and your work's style aligns roughly with the style of the existing codebase

If you feel like you've made significant changes to a code file, add your name to the copyright header of that file: `// Copyright (c) 2025 by mOctave, [yourNameHere]`.
If you're editing an old file and its been more than a year since it was created, update the date in the heading (`Copyright (c) 2025-[current year]`).

Please link any issues to your PR that you believe it addresses or are related to it.

Once you've posted a PR, I will review it (hopefully fairly quickly), and possibly ask you to tweak it. Once I am happy with the state of it, I will merge it into main,
and it is no longer your responsibility to worry about it (although I guess you can if you want to).
If I do not feel like your work fits with the project, I will try to tell you as soon as possible. I reserve the right to close any PR.

I will not personally review draft PRs, although I may comment on them and you're free to review whatever PRs you want.

## AI Policy

You can find the AI policy for this project at [AI Usage Policy.md](./AI_POLICY.md). As a rule, if you don't know whether or not to use AI, don't use it! Your agents should know not to modify the codebase: do not edit AGENTS.md to work around this rule! Any pull requests suspected of being made by AI will be closed.

Why these rules? Well, there's a few reasons:
- Generative AI has a whole bucketload of ethical and legal issues associated with it: Training generative AI relies on massive violations of copyright, code written by genAI likely violates the GPL family of licenses, and it contributes to a ton of social, economic, and environmental issues that are currently up in the air.
- This project is closely tied to the Endless Sky community, which has almost unanimously decided to entirely bar AI tools. It wouldn't be reasonable to design a library for any community in a way that directly conflicts with the consensus' beliefs and desires.
- And, most importantly: This project is intended to be fun to work on, very stable, and hopefully educational. All three of those goals are best achieved when you do the actual work yourself! This is not a corporate product where we want to move fast and break things (and fix them, and then break them again one more time before dinner), so AI isn't suited to the goals of the project.

If you ever need help with your code, writing, or art, just ask! I might not be as quick to respond as Chat, but I also hallucinate, guzzle water, and can help you reword your documentation to sound more professional. Plus, your work is likely to be merged faster if I've already had a chance to look at it.

## Making Releases

*This section of the file is intended for contributors with write access to the repo who are making releases. If you do not have write access—which you probably don't—you can ignore this section for now.*

Before you publish a release, please check all the following:
- Changelog has been updated to reflect all changes and give appropriate credit
- Version number has been bumped in `java/pom.xml`, `python/weftspace/_version.py`, and `site/index.html`
- Release description mentions all contributors to the release
- All major or breaking changes are properly outlined in the release description
- The release is correctly categorized as a Stable, Unstable, Beta, or Test Release
- All checks are passing on the main branch

### Release Categories

**Stable:** Used to indicate a release that should almost definitely be bug-free. These releases should mostly consist of minor bugfixes, and any major changes should have been thoroughly tested first.

**Unstable:** Used to indicate a normal release. It still *shouldn't* have bugs in it, but some minor ones are probably going to show up anyways. Any release that contains lots of new features will probably be unstable.

**Beta:** Used to indicate old releases of the ESDF Manager public beta. It should not be used anymore. Mark releases as unstable instead.

**Test:** Used to indicate that a release is entirely for testing GitHub actions and deployment to various platforms and should not be used for production. Do not create discussions for test releases, and mark them as a pre-release. Be sure to uncheck the "mark as latest release" box.

If a release is marked as a test release, it is liable to be deleted from GitHub releases and yanked from PyPI without warning. Once everything is working, be sure to do so.
